package com.marketdata.application.ports.in;

import com.marketdata.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceUseCaseTest {

    @Mock
    private PriceUseCase priceUseCase;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(priceUseCase);
    }

    @Test
    void getPrices_returns_expected_prices() {
        // Given
        String ticker = "AAPL";
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        List<Price> expectedPrices = List.of(
            Price.builder()
                .stockId(1L)
                .date(LocalDate.of(2023, 1, 2))
                .closePrice(new BigDecimal("150.00"))
                .build(),
            Price.builder()
                .stockId(1L)
                .date(LocalDate.of(2023, 2, 1))
                .closePrice(new BigDecimal("155.00"))
                .build()
        );

        // When
        when(priceUseCase.getPrices(ticker, from, to)).thenReturn(expectedPrices);

        // Then
        List<Price> result = priceUseCase.getPrices(ticker, from, to);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getStockId());
        assertEquals("150.00", result.get(0).getClosePrice().toString());
        verify(priceUseCase).getPrices(eq(ticker), eq(from), eq(to));
    }

    @Test
    void getLatestPrice_returns_latest_price() {
        // Given
        String ticker = "GOOGL";
        Price expectedPrice = Price.builder()
            .stockId(2L)
            .date(LocalDate.of(2023, 12, 31))
            .closePrice(new BigDecimal("2800.00"))
            .build();

        // When
        when(priceUseCase.getLatestPrice(ticker)).thenReturn(expectedPrice);

        // Then
        Price result = priceUseCase.getLatestPrice(ticker);

        assertNotNull(result);
        assertEquals(2L, result.getStockId());
        assertEquals("2800.00", result.getClosePrice().toString());
        verify(priceUseCase).getLatestPrice(eq(ticker));
    }

    @Test
    void getPrices_returns_empty_list_when_no_prices_found() {
        // Given
        String ticker = "MSFT";
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        // When
        when(priceUseCase.getPrices(ticker, from, to)).thenReturn(List.of());

        // Then
        List<Price> result = priceUseCase.getPrices(ticker, from, to);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(priceUseCase).getPrices(eq(ticker), eq(from), eq(to));
    }

    @Test
    void getLatestPrice_returns_null_when_no_price_found() {
        // Given
        String ticker = "AMZN";

        // When
        when(priceUseCase.getLatestPrice(ticker)).thenReturn(null);

        // Then
        Price result = priceUseCase.getLatestPrice(ticker);

        assertNull(result);
        verify(priceUseCase).getLatestPrice(eq(ticker));
    }

    @Test
    void getPrices_with_invalid_ticker_returns_empty_list() {
        // Given
        String invalidTicker = "INVALID";
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        // When
        when(priceUseCase.getPrices(invalidTicker, from, to)).thenReturn(List.of());

        // Then
        List<Price> result = priceUseCase.getPrices(invalidTicker, from, to);

        assertTrue(result.isEmpty());
    }

    @Test
    void getLatestPrice_with_empty_string_ticker_returns_null() {
        // Given
        String emptyTicker = "";

        // When
        when(priceUseCase.getLatestPrice(emptyTicker)).thenReturn(null);

        // Then
        Price result = priceUseCase.getLatestPrice(emptyTicker);

        assertNull(result);
    }

    @Test
    void getPrices_with_null_ticker_returns_empty_list() {
        // Given
        String nullTicker = null;
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        // When
        when(priceUseCase.getPrices(nullTicker, from, to)).thenReturn(List.of());

        // Then
        List<Price> result = priceUseCase.getPrices(nullTicker, from, to);

        assertTrue(result.isEmpty());
        verify(priceUseCase).getPrices(eq(nullTicker), eq(from), eq(to));
    }

    @Test
    void getLatestPrice_with_null_ticker_returns_null() {
        // Given
        String nullTicker = null;

        // When
        when(priceUseCase.getLatestPrice(nullTicker)).thenReturn(null);

        // Then
        Price result = priceUseCase.getLatestPrice(nullTicker);

        assertNull(result);
        verify(priceUseCase).getLatestPrice(eq(nullTicker));
    }
}