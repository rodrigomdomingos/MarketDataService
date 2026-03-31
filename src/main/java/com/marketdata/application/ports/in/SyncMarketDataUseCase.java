package com.marketdata.application.ports.in;

import java.time.LocalDateTime;

public interface SyncMarketDataUseCase {

    void syncLatestPrice(String ticker);

    void syncHistoricalPrices(String ticker);

}