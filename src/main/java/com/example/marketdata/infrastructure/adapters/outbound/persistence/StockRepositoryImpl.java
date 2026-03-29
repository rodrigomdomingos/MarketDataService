package com.example.marketdata.infrastructure.adapters.outbound.persistence;

import com.example.marketdata.infrastructure.mapper.persistence.StockEntityMapper;
import com.example.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaStockRepository;
import com.example.marketdata.domain.model.Stock;
import com.example.marketdata.application.ports.out.StockRepositoryPortOut;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryPortOut {
    private final JpaStockRepository jpaStockRepository;
    private final StockEntityMapper stockEntityMapper;

    @Override
    public List<Stock> findAll() {
        return jpaStockRepository.findAll().stream()
                .map(stockEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Stock> findByTicker(String ticker) {
        return jpaStockRepository.findByTickerIgnoreCase(ticker)
                .map(stockEntityMapper::toDomain);
    }
}
