package com.marketdata.infrastructure.inbound;

import com.marketdata.application.ports.in.RawFundamentalsUseCase;
import com.marketdata.application.ports.in.StockUseCase;
import com.marketdata.application.ports.in.SyncMarketDataUseCase;
import com.marketdata.application.ports.out.PricePortOut;
import com.marketdata.domain.model.Stock;
import com.marketdata.infrastructure.adapters.inbound.scheduler.MarketDataScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketDataSchedulerTest {

    @InjectMocks
    private MarketDataScheduler marketDataScheduler;

    @Mock
    private SyncMarketDataUseCase syncMarketDataUseCase;

    @Mock
    private StockUseCase stockUseCase;

    @Mock
    private PricePortOut pricePortOut;

    @Mock
    private RawFundamentalsUseCase rawFundamentalsUseCase;

    @Test
    void testRunAllJobs() {
        Stock stock = new Stock("ITSA4.SA", "Itausa");
        stock.setHistoricalDataLoaded(false);
        when(stockUseCase.getAllStocks()).thenReturn(List.of(stock));
        when(pricePortOut.findLatestByStock(anyString())).thenReturn(Optional.empty());

        marketDataScheduler.runAllJobs();

        verify(syncMarketDataUseCase, times(1)).syncLatestPrice("ITSA4.SA");
        verify(syncMarketDataUseCase, times(1)).syncHistoricalPrices("ITSA4.SA");
        verify(rawFundamentalsUseCase, times(1)).syncRawFundamentals("ITSA4.SA");
    }

    @Test
    void testRunAllJobs_NoStocks() {
        when(stockUseCase.getAllStocks()).thenReturn(Collections.emptyList());

        marketDataScheduler.runAllJobs();

        verify(syncMarketDataUseCase, never()).syncLatestPrice(anyString());
    }
}
