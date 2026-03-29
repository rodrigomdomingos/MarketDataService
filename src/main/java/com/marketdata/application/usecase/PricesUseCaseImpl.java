package com.marketdata.application.usecase;

import com.marketdata.domain.exception.PriceNotFoundException;
import com.marketdata.domain.exception.StockNotFoundException;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.Stock;
import com.marketdata.application.ports.in.PriceUseCase;
import com.marketdata.application.ports.out.PriceRepositoryPortOut;
import com.marketdata.application.ports.out.StockRepositoryPortOut;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricesUseCaseImpl implements PriceUseCase {

    private final PriceRepositoryPortOut priceRepositoryPortOut;
    private final StockRepositoryPortOut stockRepositoryPort;

    @Override
    public List<Price> getPrices(String ticker, LocalDate from, LocalDate to) {
        log.info("Fetching prices for ticker: {} from {} to {}", ticker, from, to);

        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        List<Price> prices = priceRepositoryPortOut.findByStockAndDateRange(
                stock.getTicker(), from, to
        );

        if (prices.isEmpty()) {
            log.warn("No prices found for {} between {} and {}", ticker, from, to);
        }

        return prices;
    }

    @Override
    public Price getLatestPrice(String ticker) {
        log.info("Fetching latest price for ticker: {}", ticker);

        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        return priceRepositoryPortOut.findLatestByStock(stock.getTicker())
                .orElseThrow(() -> new PriceNotFoundException(ticker));
    }
}