package com.marketdata.application.ports.out;

import com.marketdata.domain.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockPortOut {
    List<Stock> findAll();
    Optional<Stock> findByTicker(String ticker);
    void saveAll(List<Stock> stocks);
    Stock save(Stock stock);
}
