package com.example.marketdata.infrastructure.adapters.outbound.provider;

import com.example.marketdata.domain.model.Price;
import com.example.marketdata.application.ports.out.MarketDataProviderPortOut;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class YahooFinanceProviderAdapter implements MarketDataProviderPortOut {
    @Override
    public List<Price> fetchPrices(String ticker) {
        log.info("Mock fetch prices for ticker {}", ticker);
        LocalDate today = LocalDate.now();
        return List.of(
                Price.builder()
                        .date(today.minusDays(2))
                        .closePrice(new BigDecimal("100.25"))
                        .volume(1_100_000L)
                        .build(),
                Price.builder()
                        .date(today.minusDays(1))
                        .closePrice(new BigDecimal("101.80"))
                        .volume(1_250_000L)
                        .build(),
                Price.builder()
                        .date(today)
                        .closePrice(new BigDecimal("103.10"))
                        .volume(1_500_000L)
                        .build()
        );
    }
}
