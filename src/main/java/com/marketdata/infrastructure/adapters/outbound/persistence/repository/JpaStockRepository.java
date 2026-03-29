package com.marketdata.infrastructure.adapters.outbound.persistence.repository;

import com.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByTickerIgnoreCase(String ticker);
}
