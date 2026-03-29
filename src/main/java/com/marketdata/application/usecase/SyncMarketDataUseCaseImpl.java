package com.marketdata.application.usecase;

import com.marketdata.application.ports.in.SyncMarketDataUseCase;
import com.marketdata.application.ports.out.MarketDataProviderPortOut;
import com.marketdata.application.ports.out.PriceRepositoryPortOut;
import com.marketdata.application.ports.out.StockRepositoryPortOut;
import com.marketdata.domain.exception.StockNotFoundException;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncMarketDataUseCaseImpl implements SyncMarketDataUseCase {

    private final MarketDataProviderPortOut marketDataProviderPort;
    private final PriceRepositoryPortOut priceRepositoryPort;
    private final StockRepositoryPortOut stockRepositoryPort;

    @Override
    public void syncLatestPrice(String ticker) {
        log.info("Syncing latest price for ticker: {}", ticker);

        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

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
    public void syncHistoricalPrices(String ticker, LocalDate from, LocalDate to) {
        log.info("Syncing historical prices for {} from {} to {}", ticker, from, to);

        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        List<Price> fetchedPrices =
                marketDataProviderPort.getHistoricalPrices(ticker, from, to);

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

        log.info("Saved {} historical prices for {}", enriched.size(), ticker);
    }
}