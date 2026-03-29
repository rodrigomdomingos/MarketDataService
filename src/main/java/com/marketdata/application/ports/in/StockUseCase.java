package com.marketdata.application.ports.in;

import com.marketdata.domain.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockUseCase {

    List<Stock> getAllStocks();

    Optional<Stock> getStockByTicker(String ticker);

}
