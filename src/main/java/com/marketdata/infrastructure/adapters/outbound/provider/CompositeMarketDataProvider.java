package com.marketdata.infrastructure.adapters.outbound.provider;

import com.marketdata.application.ports.out.MarketDataProviderPortOut;
import com.marketdata.domain.model.Price;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CompositeMarketDataProvider implements MarketDataProviderPortOut {

    private final YahooFinancePriceProviderAdapter yahoo;
    private final AlphaVantageProviderAdapter alpha;
    private final BrapiFinancePriceProviderAdapter brapi;

    public CompositeMarketDataProvider(YahooFinancePriceProviderAdapter yahoo, AlphaVantageProviderAdapter alpha, BrapiFinancePriceProviderAdapter brapi) {
        this.yahoo = yahoo;
        this.alpha = alpha;
        this.brapi = brapi;
    }


    @Override
    public Optional<Price> getLatestPrice(String ticker) {
        return brapi.getLatestPrice(ticker);
    }

    @Override
    public List<Price> getHistoricalPrices(String ticker) {
        return yahoo.getHistoricalPricesWithLimit(ticker);
    }
}
