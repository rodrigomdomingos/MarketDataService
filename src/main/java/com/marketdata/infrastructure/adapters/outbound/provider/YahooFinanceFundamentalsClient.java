package com.marketdata.infrastructure.adapters.outbound.provider;

import com.marketdata.domain.model.RawFundamentals;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class YahooFinanceFundamentalsClient {

    private static final Duration SESSION_TTL = Duration.ofMinutes(15);
    private static final CookieManager COOKIE_MANAGER = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .cookieHandler(COOKIE_MANAGER)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private static final Object SESSION_LOCK = new Object();
    private static volatile SessionCache sessionCache;

    public RawFundamentals fetchFundamentals(String ticker) {
        String tickerWithSuffix = ticker.contains(".") ? ticker : ticker + ".SA";
        return fetchWithSession(tickerWithSuffix, true);
    }

    private RawFundamentals fetchWithSession(String ticker, boolean allowRetry) {
        ensureSession(ticker);
        HttpResponse<String> response = getQuoteSummary(ticker, sessionCache.crumb);

        if (shouldRefreshSession(response)) {
            if (!allowRetry) {
                return emptyFundamentals(ticker);
            }
            refreshSession(ticker);
            return fetchWithSession(ticker, false);
        }

        return parseFundamentals(ticker, response.body());
    }

    private void ensureSession(String ticker) {
        SessionCache cache = sessionCache;
        if (cache != null && !cache.isExpired()) {
            return;
        }
        refreshSession(ticker);
    }

    private void refreshSession(String ticker) {
        synchronized (SESSION_LOCK) {
            SessionCache cache = sessionCache;
            if (cache != null && !cache.isExpired()) {
                return;
            }
            COOKIE_MANAGER.getCookieStore().removeAll();
            warmupQuotePage(ticker);
            String crumb = fetchCrumb();
            sessionCache = new SessionCache(crumb, Instant.now().plus(SESSION_TTL));
        }
    }

    private void warmupQuotePage(String ticker) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://finance.yahoo.com/quote/" + ticker))
                .GET()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9")
                .build();

        sendRequest(request);
    }

    private String fetchCrumb() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://query1.finance.yahoo.com/v1/test/getcrumb"))
                .GET()
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "*/*")
                .header("Referer", "https://finance.yahoo.com/")
                .build();

        HttpResponse<String> response = sendRequest(request);
        String crumb = response.body() == null ? "" : response.body().trim();
        if (crumb.isBlank()) {
            throw new IllegalStateException("Yahoo Finance crumb is blank");
        }
        return crumb;
    }

    private HttpResponse<String> getQuoteSummary(String ticker, String crumb) {
        String url = "https://query1.finance.yahoo.com/v10/finance/quoteSummary/"
                + ticker
                + "?modules=financialData,defaultKeyStatistics,summaryDetail&crumb="
                + crumb;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36")
                .header("Accept", "application/json, text/plain, */*")
                .header("Referer", "https://finance.yahoo.com/")
                .header("Origin", "https://finance.yahoo.com")
                .build();

        return sendRequest(request);
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Yahoo Finance request failed", e);
        }
    }

    private boolean shouldRefreshSession(HttpResponse<String> response) {
        if (response.statusCode() == 401 || response.statusCode() == 406) {
            return true;
        }
        String body = response.body();
        return body != null && body.contains("Invalid Crumb");
    }

    private RawFundamentals parseFundamentals(String ticker, String body) {
        if (body == null || body.isBlank()) {
            return emptyFundamentals(ticker);
        }
        JSONObject json = new JSONObject(body);
        JSONObject quoteSummary = json.optJSONObject("quoteSummary");
        if (quoteSummary == null) {
            return emptyFundamentals(ticker);
        }
        JSONArray result = quoteSummary.optJSONArray("result");
        if (result == null || result.isEmpty()) {
            return emptyFundamentals(ticker);
        }

        JSONObject root = result.optJSONObject(0);
        if (root == null) {
            return emptyFundamentals(ticker);
        }

        JSONObject summaryDetail = root.optJSONObject("summaryDetail");
        JSONObject defaultKeyStatistics = root.optJSONObject("defaultKeyStatistics");
        JSONObject financialData = root.optJSONObject("financialData");

        Double marketCap = readRaw(summaryDetail, "marketCap");
        Double dividendYield = firstNonNull(
                readRaw(summaryDetail, "trailingAnnualDividendYield"),
                readRaw(summaryDetail, "dividendYield")
        );
        Double payoutRatio = readRaw(summaryDetail, "payoutRatio");
        Double trailingPE = readRaw(summaryDetail, "trailingPE");
        Double forwardPE = readRaw(summaryDetail, "forwardPE");

        Double priceToBook = readRaw(defaultKeyStatistics, "priceToBook");
        Double profitMargins = readRaw(defaultKeyStatistics, "profitMargins");
        Double netIncome = readRaw(defaultKeyStatistics, "netIncomeToCommon");

        Double currentPrice = readRaw(financialData, "currentPrice");
        Double revenue = readRaw(financialData, "totalRevenue");
        Double returnOnEquity = readRaw(financialData, "returnOnEquity");
        Double returnOnAssets = readRaw(financialData, "returnOnAssets");
        Double totalDebt = readRaw(financialData, "totalDebt");
        Double totalCash = readRaw(financialData, "totalCash");
        Double operatingCashflow = readRaw(financialData, "operatingCashflow");
        Double revenueGrowth = readRaw(financialData, "revenueGrowth");
        Double earningsGrowth = readRaw(financialData, "earningsGrowth");
        
        Double pegRatio = readRaw(defaultKeyStatistics, "pegRatio");
        Double freeCashFlow = readRaw(financialData, "freeCashflow");

        return new RawFundamentals(
                null,
                null,
                currentPrice,
                marketCap,
                trailingPE,
                forwardPE,
                priceToBook,
                returnOnEquity,
                returnOnAssets,
                profitMargins,
                dividendYield,
                payoutRatio,
                revenue,
                netIncome,
                totalDebt,
                totalCash,
                operatingCashflow,
                revenueGrowth,
                earningsGrowth,
                "YAHOO_FINANCE",
                pegRatio,
                freeCashFlow
        );
    }

    private Double readRaw(JSONObject parent, String key) {
        if (parent == null) {
            return null;
        }
        JSONObject value = parent.optJSONObject(key);
        if (value == null) {
            return null;
        }
        Object raw = value.opt("raw");
        if (raw == null || raw == JSONObject.NULL) {
            return null;
        }
        if (raw instanceof Number) {
            return ((Number) raw).doubleValue();
        }
        if (raw instanceof String) {
            String str = ((String) raw).trim();
            if (str.isEmpty()) {
                return null;
            }
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Double firstNonNull(Double first, Double second) {
        return first != null ? first : second;
    }

    private RawFundamentals emptyFundamentals(String ticker) {
        return new RawFundamentals(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "YAHOO_FINANCE",
                null,
                null
        );
    }

    private static final class SessionCache {
        private final String crumb;
        private final Instant expiresAt;

        private SessionCache(String crumb, Instant expiresAt) {
            this.crumb = Objects.requireNonNull(crumb, "crumb");
            this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt");
        }

        private boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}