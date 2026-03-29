package com.example.marketdata.application.ports.in;

import com.example.marketdata.domain.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockUseCase {
    List<Stock> getAllStocks();
    Optional<Stock> getStockByTicker(String ticker);
}
