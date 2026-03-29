package com.example.marketdata.application.usecase;

import com.example.marketdata.application.exception.PriceNotFoundException;
import com.example.marketdata.application.exception.StockNotFoundException;
import com.example.marketdata.domain.model.Price;
import com.example.marketdata.domain.model.Stock;
import com.example.marketdata.application.ports.in.PriceUseCase;
import com.example.marketdata.application.ports.out.MarketDataProviderPortOut;
import com.example.marketdata.application.ports.out.PriceRepositoryPortOut;
import com.example.marketdata.application.ports.out.StockRepositoryPortOut;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService implements PriceUseCase {
    private final PriceRepositoryPortOut priceRepositoryPortOut;
    private final StockRepositoryPortOut stockRepositoryPort;
    private final MarketDataProviderPortOut marketDataProviderPortOut;

    @Override
    public List<Price> getPrices(String ticker, LocalDate from, LocalDate to) {
        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        List<Price> prices = priceRepositoryPortOut.findByStockAndDateRange(ticker, from, to);
        if (!prices.isEmpty()) {
            return prices;
        }

        List<Price> fetched = marketDataProviderPortOut.fetchPrices(ticker);
        if (fetched.isEmpty()) {
            return prices;
        }

        List<Price> withStock = fetched.stream()
                .map(price -> price.toBuilder().stockId(stock.getId()).build())
                .toList();
        priceRepositoryPortOut.saveAll(withStock);

        return priceRepositoryPortOut.findByStockAndDateRange(ticker, from, to);
    }

    @Override
    public Price getLatestPrice(String ticker) {
        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        return priceRepositoryPortOut.findLatestByStock(ticker)
                .orElseGet(() -> {
                    List<Price> fetched = marketDataProviderPortOut.fetchPrices(ticker);
                    if (fetched.isEmpty()) {
                        throw new PriceNotFoundException(ticker);
                    }
                    List<Price> withStock = fetched.stream()
                            .map(price -> price.toBuilder().stockId(stock.getId()).build())
                            .toList();
                    priceRepositoryPortOut.saveAll(withStock);
                    return priceRepositoryPortOut.findLatestByStock(ticker)
                            .orElseThrow(() -> new PriceNotFoundException(ticker));
                });
    }
}
