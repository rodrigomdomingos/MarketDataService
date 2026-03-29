package com.marketdata.application.usecase;

import com.marketdata.domain.model.Stock;
import com.marketdata.application.ports.out.StockRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockUseCaseImplTest {

    @Mock
    private StockRepositoryPortOut stockRepositoryPort;

    @InjectMocks
    private StockUseCaseImpl stockService;

    private List<Stock> createStocks() {
        List<Stock> stocks = new ArrayList<>();
        Stock stock1 = new Stock("AAPL", "Apple Inc.");
        Stock stock2 = new Stock("GOOGL", "Alphabet Inc.");
        stocks.add(stock1);
        stocks.add(stock2);
        return stocks;
    }

    @BeforeEach
    void setUp() {
        reset(stockRepositoryPort);
    }

    @Test
    void shouldGetAllStocks() {
        List<Stock> expectedStocks = createStocks();
        when(stockRepositoryPort.findAll()).thenReturn(expectedStocks);

        List<Stock> actualStocks = stockService.getAllStocks();

        assertThat(actualStocks).isEqualTo(expectedStocks);
        verify(stockRepositoryPort, times(1)).findAll();
    }

    @Test
    void shouldGetStockByTicker() {
        List<Stock> stocks = createStocks();
        Stock expectedStock = new Stock("AAPL", "Apple Inc.");
        when(stockRepositoryPort.findByTicker(anyString())).thenReturn(Optional.of(expectedStock));

        Optional<Stock> actualStock = stockService.getStockByTicker("AAPL");

        assertThat(actualStock).isPresent().contains(expectedStock);
        verify(stockRepositoryPort, times(1)).findByTicker("AAPL");
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistingTicker() {
        when(stockRepositoryPort.findByTicker(anyString())).thenReturn(Optional.empty());

        Optional<Stock> actualStock = stockService.getStockByTicker("MISSING");

        assertThat(actualStock).isEmpty();
        verify(stockRepositoryPort, times(1)).findByTicker("MISSING");
    }
}
