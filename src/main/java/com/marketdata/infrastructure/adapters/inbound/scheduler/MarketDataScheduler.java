package com.marketdata.infrastructure.adapters.inbound.scheduler;

import com.google.common.util.concurrent.RateLimiter;
import com.marketdata.application.ports.in.StockUseCase;
import com.marketdata.application.ports.in.SyncMarketDataUseCase;
import com.marketdata.application.ports.out.PricePortOut;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDataScheduler {

    private final SyncMarketDataUseCase syncMarketDataUseCase;
    private final StockUseCase stockUseCase;
    private final PricePortOut pricePortOut;

    private final RateLimiter rateLimiter = RateLimiter.create(30.0 / 60.0);
    private final AtomicInteger historicalPriceIndex = new AtomicInteger(0);

    @Scheduled(cron = "0 0 19 * * *")
    public void syncLatestPrices() {
        log.info("Starting scheduled sync for latest prices");

        LocalDate today = LocalDate.now();

        try {
            List<Stock> stocks = stockUseCase.getAllStocks();

            if (stocks == null || stocks.isEmpty()) {
                log.info("No stocks found to sync latest prices");
                return;
            }

            final int total = stocks.size();
            int processed = 0;

            for (Stock stock : stocks) {
                processed++;

                // Use tryAcquire with a small timeout so scheduler doesn't block indefinitely
                boolean permit = rateLimiter.tryAcquire(2, TimeUnit.SECONDS);
                if (!permit) {
                    log.warn("Rate limiter prevented syncing {} this cycle; skipping", stock.getTicker());
                    continue;
                }

                try {
                    Optional<Price> latestPriceOpt = pricePortOut.findLatestByStock(stock.getTicker());

                    if (latestPriceOpt.isPresent()) {
                        LocalDate priceDate = latestPriceOpt.get().getDate().toLocalDate();
                        if (priceDate.isEqual(today)) {
                            log.debug("Skipping {} as it already has a price for today (date={})", stock.getTicker(), priceDate);
                            continue;
                        }
                    }

                    syncMarketDataUseCase.syncLatestPrice(stock.getTicker());

                } catch (Exception e) {
                    log.error("Failed to sync ticker {}", stock.getTicker(), e);
                }

                if (processed % 50 == 0 || processed == total) {
                    log.info("Processed {}/{} stocks for latest price sync", processed, total);
                }
            }

        } catch (Exception e) {
            log.error("Error during scheduled sync", e);
        }

        log.info("Finished scheduled sync");
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void syncHistoricalPrices() {
        log.info("Starting scheduled sync for historical prices");

        try {
            List<Stock> stocks = stockUseCase.getAllStocks().stream()
                    .filter(stock -> !stock.isHistoricalDataLoaded())
                    .toList();

            if (stocks.isEmpty()) {
                return;
            }

            int index = historicalPriceIndex.getAndIncrement() % stocks.size();
            Stock stock = stocks.get(index);

            try {
                syncMarketDataUseCase.syncHistoricalPrices(stock.getTicker());
            } catch (Exception e) {
                log.error("Failed to sync historical ticker {}", stock.getTicker(), e);
            }

        } catch (Exception e) {
            log.error("Error during scheduled historical sync", e);
        }

        log.info("Finished scheduled historical sync");
    }
}
