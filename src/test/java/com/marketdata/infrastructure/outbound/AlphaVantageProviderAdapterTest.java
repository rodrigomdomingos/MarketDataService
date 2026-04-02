package com.marketdata.infrastructure.outbound;

import com.marketdata.domain.model.Price;
import com.marketdata.infrastructure.adapters.outbound.provider.AlphaVantageProviderAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlphaVantageProviderAdapterTest {

    @InjectMocks
    private AlphaVantageProviderAdapter alphaVantageProviderAdapter;

    @Mock
    private AlphaVantageProviderAdapter mockAdapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(alphaVantageProviderAdapter, "baseUrl", "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=");
        ReflectionTestUtils.setField(alphaVantageProviderAdapter, "apiKey", "TEST_API_KEY");
    }

    @Test
    void testGetLatestPrice_Success() {
        String ticker = "ITSA4.SA";
        Price expectedPrice = new Price(null, null, OffsetDateTime.now(), new BigDecimal("8.50"), 1000L);

        when(mockAdapter.getLatestPrice(ticker)).thenReturn(Optional.of(expectedPrice));

        Optional<Price> actualPrice = mockAdapter.getLatestPrice(ticker);

        assertTrue(actualPrice.isPresent());
        assertEquals(expectedPrice.getClosePrice(), actualPrice.get().getClosePrice());
    }

    @Test
    void testGetLatestPrice_ApiFailure() {
        String ticker = "FAIL.SA";

        when(mockAdapter.getLatestPrice(ticker)).thenReturn(Optional.empty());

        Optional<Price> result = mockAdapter.getLatestPrice(ticker);

        assertTrue(result.isEmpty());
    }
}
