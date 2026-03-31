package com.marketdata.infrastructure.adapters.outbound.provider;

import com.marketdata.application.ports.out.MarketDataProviderPortOut;
import com.marketdata.domain.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AlphaVantageProviderAdapter {

    @Value("${marketdata.provider.alpha-vantage.base-url}")
    private String baseUrl;

    @Value("${marketdata.provider.alpha-vantage.api-key}")
    private String apiKey;

    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000)
    )
    public Optional<Price> getLatestPrice(String ticker) {
        try {
            String body = fetchPriceJson(ticker);
            return Optional.of(mapToDomain(ticker, body));
        } catch (Exception e) {
            log.error("Failed to fetch latest price for {}", ticker, e);
            return Optional.empty();
        }
    }

    @Recover
    public Optional<Price> recover(Exception e, String ticker) {
        log.error("All retry attempts failed for ticker {}. Returning empty.", ticker, e);
        return Optional.empty();
    }

    private String fetchPriceJson(String ticker) {
        String formattedTicker = ticker.endsWith(".SA") ? ticker : ticker + ".SA";
        String url = baseUrl + formattedTicker + "&apikey=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("AlphaVantage response status for {}: {}, body: {}", ticker, response.statusCode(), response.body());

            String body = response.body();

            if (body == null || body.isBlank()) {
                throw new RuntimeException("Empty response from AlphaVantage");
            }

            if (!body.trim().startsWith("{")) {
                throw new RuntimeException("Invalid JSON response: " + body.substring(0, Math.min(100, body.length())));
            }

            return body;

        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("HTTP request failed for ticker " + ticker, e);
        }
    }

    private Price mapToDomain(String ticker, String body) {
        JSONObject json = new JSONObject(body);

        if (json.has("Note") || json.has("Error Message")) {
            log.warn("AlphaVantage API limit/error for {}: {}", ticker, body);
            throw new IllegalStateException("API error"); // handled above
        }

        JSONObject quote = json.optJSONObject("Global Quote");

        if (quote == null || quote.isEmpty()) {
            throw new IllegalStateException("Missing quote");
        }

        BigDecimal closePrice = new BigDecimal(quote.getString("05. price"));
        Long volume = quote.optLong("06. volume");
        LocalDateTime date = LocalDate.parse(quote.getString("07. latest trading day")).atStartOfDay();

        return new Price(null, null, date, closePrice, volume);
    }

    private List<Price> mapHistoricalToDomain(
            String ticker,
            String body,
            LocalDateTime from,
            LocalDateTime to
    ) {
        try {
            JSONObject json = new JSONObject(body);

            if (json.has("Note") || json.has("Error Message")) {
                throw new RuntimeException("AlphaVantage API error: " + body);
            }

            JSONObject timeSeries = json.getJSONObject("Time Series (Daily)");

            return timeSeries.keySet().stream()
                    .map(dateStr -> LocalDate.parse(dateStr).atStartOfDay())
                    .filter(date -> !date.isBefore(from) && !date.isAfter(to))
                    .map(date -> {
                        JSONObject daily = timeSeries.getJSONObject(date.toLocalDate().toString());

                        BigDecimal close = new BigDecimal(daily.getString("4. close"));
                        Long volume = daily.optLong("5. volume");

                        return new Price(null, null, date, close, volume);
                    })
                    .sorted((p1, p2) -> p1.getDate().compareTo(p2.getDate()))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse historical data", e);
        }
    }

}