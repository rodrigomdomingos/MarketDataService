package com.marketdata.application.ports.in;

import java.time.LocalDate;

public interface SyncMarketDataUseCase {

    void syncLatestPrice(String ticker);

    void syncHistoricalPrices(String ticker, LocalDate from, LocalDate to);

}