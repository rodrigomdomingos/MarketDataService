package com.marketdata.infrastructure.adapters.outbound.persistence.repository;

import com.marketdata.infrastructure.adapters.outbound.persistence.entity.PriceEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    List<PriceEntity> findByStock_TickerIgnoreCaseAndDateBetweenOrderByDateAsc(
            String ticker,
            LocalDateTime from,
            LocalDateTime to
    );

    Optional<PriceEntity> findFirstByStock_TickerIgnoreCaseOrderByDateDesc(String ticker);
}
