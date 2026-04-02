package com.marketdata.infrastructure.adapters.inbound.scheduler;

import com.google.common.util.concurrent.RateLimiter;
import com.marketdata.application.ports.in.RawFundamentalsUseCase;
import com.marketdata.application.ports.in.StockUseCase;
import com.marketdata.application.ports.in.SyncMarketDataUseCase;
import com.marketdata.application.ports.out.PricePortOut;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDataScheduler {

    private final SyncMarketDataUseCase syncMarketDataUseCase;
    private final StockUseCase stockUseCase;
    private final PricePortOut pricePortOut;
    private final RawFundamentalsUseCase rawFundamentalsUseCase;

    private final RateLimiter rateLimiter = RateLimiter.create(30.0 / 60.0);

    private final ZoneId brazilZoneId = ZoneId.of("America/Sao_Paulo");

    @EventListener(ApplicationReadyEvent.class)
    public void runAtStartup() {
        log.info("Running Market Data sync at startup");
        runAllJobs();
    }

    @Scheduled(cron = "0 0 19 * * *", zone = "America/Sao_Paulo")
    public void runAllJobs() {
        log.info("Starting Market Data scheduled jobs");

        try {
            List<Stock> stocks = stockUseCase.getAllStocks();

            if (stocks == null || stocks.isEmpty()) {
                log.info("No stocks found");
                return;
            }

            syncLatestPrices(stocks);
            syncHistoricalPrices(stocks);
            syncRawFundamentals(stocks);

        } catch (Exception e) {
            log.error("Error executing scheduled jobs", e);
        }

        log.info("Finished Market Data scheduled jobs");
    }

    // =========================
    // Jobs
    // =========================

    private void syncLatestPrices(List<Stock> stocks) {
        log.info("Starting latest prices sync");

        LocalDate today = LocalDate.now(brazilZoneId);

        processAllStocks("latest prices", stocks, stock -> {
            Optional<Price> latestPriceOpt = pricePortOut.findLatestByStock(stock.getTicker());

            if (latestPriceOpt.isPresent()) {
                LocalDate priceDate = latestPriceOpt.get().getDate().toLocalDate();
                if (priceDate.isEqual(today)) {
                    log.debug("Skipping {} (already up-to-date)", stock.getTicker());
                    return;
                }
            }

            rateLimiter.acquire();
            syncMarketDataUseCase.syncLatestPrice(stock.getTicker());
        });

        log.info("Finished latest prices sync");
    }

    private void syncHistoricalPrices(List<Stock> stocks) {
        log.info("Starting historical prices sync");

        List<Stock> toProcess = stocks.stream()
                .filter(stock -> !stock.isHistoricalDataLoaded())
                .toList();

        processAllStocks("historical prices", toProcess, stock -> {
            rateLimiter.acquire();
            syncMarketDataUseCase.syncHistoricalPrices(stock.getTicker());
        });

        log.info("Finished historical prices sync");
    }

    private void syncRawFundamentals(List<Stock> stocks) {
        log.info("Starting raw fundamentals sync");

        processAllStocks("raw fundamentals", stocks, stock -> {
            rateLimiter.acquire();
            rawFundamentalsUseCase.syncRawFundamentals(stock.getTicker());
        });

        log.info("Finished raw fundamentals sync");
    }

    // =========================
    // Core processor
    // =========================

    private void processAllStocks(
            String jobName,
            List<Stock> stocks,
            Consumer<Stock> action
    ) {
        final int total = stocks.size();
        int processed = 0;

        for (Stock stock : stocks) {
            processed++;

            try {
                action.accept(stock);
            } catch (Exception e) {
                log.error("Failed {} for ticker {}", jobName, stock.getTicker(), e);
            }

            if (processed % 10 == 0 || processed == total) {
                log.info("Processed {}/{} for {}", processed, total, jobName);
            }
        }
    }
}
