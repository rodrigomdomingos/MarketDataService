package com.marketdata.application.service;

import com.marketdata.application.ports.out.MarketDataUpdatedPortOut;
import com.marketdata.application.ports.out.PricePortOut;
import com.marketdata.application.ports.out.RawFundamentalsPortOut;
import com.marketdata.application.ports.out.StockPortOut;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.RawFundamentals;
import com.marketdata.domain.model.Stock;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketDataOrchestrator {

    private final PricePortOut pricePortOut;
    private final StockPortOut stockPortOut;
    private final RawFundamentalsPortOut rawFundamentalsPortOut;
    private final MarketDataUpdatedPortOut marketDataUpdatedPortOut;

    @Transactional
    public void saveLatestPrice(Price price) {
        pricePortOut.save(price);
        marketDataUpdatedPortOut.publishAfterCommit(price.getStockId(), price.getDate());
    }

    @Transactional
    public void saveHistoricalPrices(Stock stock, List<Price> prices) {
        pricePortOut.saveAll(prices);
        stock.setHistoricalDataLoaded(true);
        stockPortOut.save(stock);

        OffsetDateTime latestSnapshot = prices.stream()
                .map(Price::getDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalStateException("Historical prices must contain a valid snapshot date"));

        marketDataUpdatedPortOut.publishAfterCommit(stock.getId(), latestSnapshot);
    }

    @Transactional
    public void saveRawFundamentals(RawFundamentals rawFundamentals) {
        RawFundamentals saved = rawFundamentalsPortOut.save(rawFundamentals);
        marketDataUpdatedPortOut.publishAfterCommit(saved.getStockId(), saved.getReferenceDate());
    }
}
