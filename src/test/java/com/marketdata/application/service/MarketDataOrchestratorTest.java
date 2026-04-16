package com.marketdata.application.service;

import com.marketdata.application.ports.out.MarketDataUpdatedPortOut;
import com.marketdata.application.ports.out.PricePortOut;
import com.marketdata.application.ports.out.RawFundamentalsPortOut;
import com.marketdata.application.ports.out.StockPortOut;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.RawFundamentals;
import com.marketdata.domain.model.Stock;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketDataOrchestratorTest {

    @Mock
    private PricePortOut pricePortOut;

    @Mock
    private StockPortOut stockPortOut;

    @Mock
    private RawFundamentalsPortOut rawFundamentalsPortOut;

    @Mock
    private MarketDataUpdatedPortOut marketDataUpdatedPortOut;

    @InjectMocks
    private MarketDataOrchestrator marketDataOrchestrator;

    @Test
    void saveLatestPricePublishesAfterCommit() {
        OffsetDateTime snapshotAt = OffsetDateTime.parse("2026-04-15T20:00:00Z");
        Price price = Price.builder()
                .stockId(10L)
                .date(snapshotAt)
                .build();

        marketDataOrchestrator.saveLatestPrice(price);

        verify(pricePortOut).save(price);
        verify(marketDataUpdatedPortOut).publishAfterCommit(10L, snapshotAt);
    }

    @Test
    void saveHistoricalPricesPublishesLatestSnapshotAndUpdatesStock() {
        OffsetDateTime oldSnapshot = OffsetDateTime.parse("2026-04-14T20:00:00Z");
        OffsetDateTime newSnapshot = OffsetDateTime.parse("2026-04-15T20:00:00Z");
        Price oldPrice = Price.builder().stockId(10L).date(oldSnapshot).build();
        Price newPrice = Price.builder().stockId(10L).date(newSnapshot).build();

        Stock stock = new Stock("PETR4", "Petrobras");
        stock.setId(10L);
        stock.setHistoricalDataLoaded(false);

        marketDataOrchestrator.saveHistoricalPrices(stock, List.of(oldPrice, newPrice));

        verify(pricePortOut).saveAll(List.of(oldPrice, newPrice));
        verify(stockPortOut).save(stock);
        verify(marketDataUpdatedPortOut).publishAfterCommit(10L, newSnapshot);
    }

    @Test
    void saveRawFundamentalsPublishesAfterCommit() {
        OffsetDateTime referenceDate = OffsetDateTime.parse("2026-04-15T20:00:00Z");
        RawFundamentals fundamentals = new RawFundamentals(
                20L, referenceDate, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, "YAHOO_FINANCE", null, null
        );
        when(rawFundamentalsPortOut.save(fundamentals)).thenReturn(fundamentals);

        marketDataOrchestrator.saveRawFundamentals(fundamentals);

        verify(rawFundamentalsPortOut).save(fundamentals);
        verify(marketDataUpdatedPortOut).publishAfterCommit(20L, referenceDate);
    }
}
