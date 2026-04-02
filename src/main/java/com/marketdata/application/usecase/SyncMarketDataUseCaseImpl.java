package com.marketdata.application.usecase;

import com.marketdata.application.ports.in.SyncMarketDataUseCase;
import com.marketdata.application.ports.out.MarketDataProviderPortOut;
import com.marketdata.application.ports.out.PricePortOut;
import com.marketdata.application.ports.out.StockPortOut;
import com.marketdata.domain.exception.StockNotFoundException;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncMarketDataUseCaseImpl implements SyncMarketDataUseCase {

    private final MarketDataProviderPortOut marketDataProviderPort;
    private final PricePortOut priceRepositoryPort;
    private final StockPortOut stockRepositoryPort;

    @Override
    public void syncLatestPrice(String ticker) {
        log.info("Syncing latest price for ticker: {}", ticker);

        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        OffsetDateTime today = OffsetDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).withNano(0);
        Optional<Price> priceForToday = priceRepositoryPort.findByStockIdAndDate(stock.getId(), today);
        if (priceForToday.isPresent()) {
            log.warn("Price for {} on {} already exists, skipping sync.", ticker, today);
            return;
        }

        Optional<Price> priceOpt = marketDataProviderPort.getLatestPrice(ticker);

        if (priceOpt.isEmpty()) {
            log.warn("No price returned from provider for {}", ticker);
            return;
        }

        Price enriched = priceOpt.get().toBuilder()
                .stockId(stock.getId())
                .build();

        priceRepositoryPort.save(enriched);

        log.info("Saved latest price for {}", ticker);
    }

    @Override
    public void syncHistoricalPrices(String ticker) {
        log.info("Syncing historical prices for {}", ticker);

        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        List<Price> fetchedPrices = marketDataProviderPort.getHistoricalPrices(ticker);

        if (fetchedPrices.isEmpty()) {
            log.warn("No historical prices returned from provider for {}", ticker);
            return;
        }

        List<Price> enriched = fetchedPrices.stream()
                .map(price -> price.toBuilder()
                        .stockId(stock.getId())
                        .build())
                .toList();

        priceRepositoryPort.saveAll(enriched);

        stock.setHistoricalDataLoaded(true);
        stockRepositoryPort.save(stock);

        log.info("Saved {} historical prices for {}", enriched.size(), ticker);
    }
}
