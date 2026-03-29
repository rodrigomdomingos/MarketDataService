package com.marketdata.infrastructure.adapters.inbound.batch;

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

    @Scheduled(cron = "0 * * * * *")
    public void syncLatestPrices() {
        log.info("Starting scheduled sync for latest prices");

        try {
            List<Stock> stocks = stockUseCase.getAllStocks();

            for (Stock stock : stocks) {
                try {
                    syncMarketDataUseCase.syncLatestPrice(stock.getTicker());
                    Thread.sleep(10000);

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
