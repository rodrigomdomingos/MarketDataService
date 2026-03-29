package com.example.marketdata.application.usecase;

import com.example.marketdata.domain.model.Stock;
import com.example.marketdata.application.ports.in.StockUseCase;
import com.example.marketdata.application.ports.out.StockRepositoryPortOut;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService implements StockUseCase {
    private final StockRepositoryPortOut stockRepositoryPort;

    @Override
    public List<Stock> getAllStocks() {
        return stockRepositoryPort.findAll();
    }

    @Override
    public Optional<Stock> getStockByTicker(String ticker) {
        return stockRepositoryPort.findByTicker(ticker);
    }
}
