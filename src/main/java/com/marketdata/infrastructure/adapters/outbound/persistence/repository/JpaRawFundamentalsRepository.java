package com.marketdata.infrastructure.adapters.outbound.persistence.repository;

import com.marketdata.infrastructure.adapters.outbound.persistence.entity.RawFundamentalsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface JpaRawFundamentalsRepository extends JpaRepository<RawFundamentalsEntity, Long> {

    Optional<RawFundamentalsEntity> findByStockIdAndReferenceDate(Long stockId, LocalDate referenceDate);
}
