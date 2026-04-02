package com.marketdata.application.ports.out;

import com.marketdata.domain.model.Price;

import java.util.List;
import java.util.Optional;

public interface MarketDataProviderPortOut {

    Optional<Price> getLatestPrice(String ticker);
    List<Price> getHistoricalPrices(String ticker);

}
