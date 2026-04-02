package com.marketdata.application.ports.in;

public interface SyncMarketDataUseCase {

    void syncLatestPrice(String ticker);

    void syncHistoricalPrices(String ticker);

}
