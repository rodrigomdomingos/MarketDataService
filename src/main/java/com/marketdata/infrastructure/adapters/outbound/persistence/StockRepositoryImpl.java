package com.marketdata.infrastructure.adapters.outbound.persistence;

import com.marketdata.infrastructure.adapters.outbound.persistence.mapper.StockEntityMapper;
import com.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaStockRepository;
import com.marketdata.domain.model.Stock;
import com.marketdata.application.ports.out.StockRepositoryPortOut;
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
