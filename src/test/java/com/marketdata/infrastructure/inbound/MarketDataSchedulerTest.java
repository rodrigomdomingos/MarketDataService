package com.marketdata.infrastructure.inbound;

import com.marketdata.application.ports.in.StockUseCase;
import com.marketdata.application.ports.in.SyncMarketDataUseCase;
import com.marketdata.domain.model.Stock;
import com.marketdata.infrastructure.adapters.inbound.scheduler.MarketDataScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketDataSchedulerTest {

    @InjectMocks
    private MarketDataScheduler marketDataScheduler;

    @Mock
    private SyncMarketDataUseCase syncMarketDataUseCase;

    @Mock
    private StockUseCase stockUseCase;

    @Test
    void testSyncLatestPrices() {
        Stock stock = new Stock("ITSA4.SA", "Itausa");
        when(stockUseCase.getAllStocks()).thenReturn(List.of(stock));

        marketDataScheduler.syncLatestPrices();

        verify(syncMarketDataUseCase, times(1)).syncLatestPrice("ITSA4.SA");
    }

    @Test
    void testSyncLatestPrices_NoStocks() {
        when(stockUseCase.getAllStocks()).thenReturn(Collections.emptyList());

        marketDataScheduler.syncLatestPrices();

        verify(syncMarketDataUseCase, never()).syncLatestPrice(anyString());
    }
}
