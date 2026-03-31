package com.marketdata.infrastructure.adapters.outbound.provider;

import com.marketdata.domain.model.Price;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinancePriceProviderAdapter {

    private static final String BASE_URL = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=200d&interval=1d";
    private static final int LIMIT = 200;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public YahooFinancePriceProviderAdapter(RestTemplate restTemplate,
                                            ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Price> getHistoricalPricesWithLimit(String ticker) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                String url = String.format(BASE_URL, ticker + ".SA");

                HttpHeaders headers = new HttpHeaders();
                headers.set("User-Agent", "Mozilla/5.0");
                headers.set("Accept", "application/json");

                HttpEntity<Void> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        String.class
                );

                String body = response.getBody();
                JsonNode root = objectMapper.readTree(body);

                JsonNode result = root.path("chart").path("result").get(0);

                JsonNode timestamps = result.path("timestamp");
                JsonNode quote = result.path("indicators").path("quote").get(0);

                JsonNode closes = quote.path("close");
                JsonNode volumes = quote.path("volume");

                List<Price> prices = new ArrayList<>();

                int size = timestamps.size();
                int start = Math.max(0, size - LIMIT);

                for (int i = start; i < size; i++) {

                    if (closes.get(i).isNull()) continue;

                    long epoch = timestamps.get(i).asLong();
                    double close = closes.get(i).asDouble();
                    long volume = volumes.get(i).asLong();

                    LocalDateTime date = Instant.ofEpochSecond(epoch)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDateTime();

                    prices.add(new Price(
                            null,
                            null, // stockId will be set later
                            date,
                            BigDecimal.valueOf(close),
                            volume
                    ));
                }

                return prices;

            } catch (HttpClientErrorException.TooManyRequests e) {
                attempts++;
                try {
                    Thread.sleep(2000 * attempts); // exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching data from Yahoo Finance", e);
            }
        }
        throw new RuntimeException("Failed to fetch data from Yahoo Finance after multiple retries");
    }
}
