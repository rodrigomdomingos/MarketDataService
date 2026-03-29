package com.marketdata.infrastructure.adapters.inbound.scheduler;

import com.google.common.util.concurrent.RateLimiter;
import com.marketdata.application.ports.in.StockUseCase;
import com.marketdata.application.ports.in.SyncMarketDataUseCase;
import com.marketdata.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDataScheduler {

    private final SyncMarketDataUseCase syncMarketDataUseCase;
    private final StockUseCase stockUseCase;

    private final RateLimiter rateLimiter = RateLimiter.create(5.0 / 60.0);

    @Scheduled(cron = "0 0 9 * * *")
    public void syncLatestPrices() {
        log.info("Starting scheduled sync for latest prices");

        try {
            List<Stock> stocks = stockUseCase.getAllStocks();

            for (Stock stock : stocks) {
                rateLimiter.acquire();
                try {
                    syncMarketDataUseCase.syncLatestPrice(stock.getTicker());
                } catch (Exception e) {
                    log.error("Failed to sync ticker {}", stock.getTicker(), e);
                }
            }

        } catch (Exception e) {
            log.error("Error during scheduled sync", e);
        }

        log.info("Finished scheduled sync");
    }
}
