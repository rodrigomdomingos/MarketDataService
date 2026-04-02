package com.marketdata.infrastructure.adapters.outbound.provider;

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
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Component
public class BrapiFinancePriceProviderAdapter {

    @Value("${marketdata.provider.brapi.base-url}")
    private String baseUrl;

    @Value("${marketdata.provider.brapi.api-key}")
    private String apiKey;

    @Retryable(
            retryFor = { Exception.class },
            backoff = @Backoff(delay = 5000)
    )
    public Optional<Price> getLatestPrice(String ticker) {
        try {
            String body = fetchPriceJson(ticker);
            return Optional.of(mapToDomain(body));
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
        String formattedTicker = ticker.endsWith(".SA") ? ticker.replace(".SA", "") : ticker;
        String url = baseUrl + formattedTicker + "?fundamental=true&token=" + apiKey;

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Brapi response status for {}: {}, body: {}", ticker, response.statusCode(), response.body());

            String body = response.body();

            if (body == null || body.isBlank()) {
                throw new RuntimeException("Empty response from Brapi");
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

    private Price mapToDomain(String body) {
        JSONObject json = new JSONObject(body);
        JSONObject result = json.getJSONArray("results").getJSONObject(0);

        BigDecimal closePrice = result.getBigDecimal("regularMarketPrice");
        long volume = result.getLong("regularMarketVolume");
        OffsetDateTime date = OffsetDateTime.parse(result.getString("regularMarketTime"));

        return new Price(null, null, date, closePrice, volume);
    }
}
