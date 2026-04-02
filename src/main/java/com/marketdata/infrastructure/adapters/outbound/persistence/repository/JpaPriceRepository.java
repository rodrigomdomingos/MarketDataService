package com.marketdata.infrastructure.adapters.outbound.persistence.repository;

import com.marketdata.infrastructure.adapters.outbound.persistence.entity.PriceEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    List<PriceEntity> findByStock_TickerIgnoreCaseAndDateBetweenOrderByDateAsc(
            String ticker,
            OffsetDateTime from,
            OffsetDateTime to
    );

    Optional<PriceEntity> findFirstByStock_TickerIgnoreCaseOrderByDateDesc(String ticker);

    Optional<PriceEntity> findByStockIdAndDate(Long stockId, OffsetDateTime date);
}
