package com.example.marketdata.infrastructure.adapters.outbound.persistence.repository;

import com.example.marketdata.infrastructure.adapters.outbound.persistence.entity.PriceEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    List<PriceEntity> findByStock_TickerIgnoreCaseAndDateBetweenOrderByDateAsc(
            String ticker,
            LocalDate from,
            LocalDate to
    );

    Optional<PriceEntity> findFirstByStock_TickerIgnoreCaseOrderByDateDesc(String ticker);
}
