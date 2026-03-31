package com.marketdata.application.usecase;

import com.marketdata.domain.model.Stock;
import com.marketdata.application.ports.in.StockUseCase;
import com.marketdata.application.ports.out.StockPortOut;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StockUseCaseImpl implements StockUseCase {
    private final StockPortOut stockRepositoryPort;

    public StockUseCaseImpl(StockPortOut stockRepositoryPort) {
        this.stockRepositoryPort = stockRepositoryPort;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepositoryPort.findAll();
    }

    @Override
    public Optional<Stock> getStockByTicker(String ticker) {
        return stockRepositoryPort.findByTicker(ticker);
    }

    @Override
    public void saveAll(List<Stock> stocks) {
        stockRepositoryPort.saveAll(stocks);
    }
}
