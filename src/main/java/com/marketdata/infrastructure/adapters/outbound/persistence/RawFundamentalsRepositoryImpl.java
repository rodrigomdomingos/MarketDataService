package com.marketdata.infrastructure.adapters.outbound.persistence;

import com.marketdata.application.ports.out.RawFundamentalsPortOut;
import com.marketdata.domain.model.RawFundamentals;
import com.marketdata.infrastructure.adapters.outbound.persistence.entity.RawFundamentalsEntity;
import com.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import com.marketdata.infrastructure.adapters.outbound.persistence.mapper.RawFundamentalsEntityMapper;
import com.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaRawFundamentalsRepository;
import com.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaStockRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RawFundamentalsRepositoryImpl implements RawFundamentalsPortOut {
    private final JpaRawFundamentalsRepository jpaRawFundamentalsRepository;
    private final JpaStockRepository jpaStockRepository;
    private final RawFundamentalsEntityMapper rawFundamentalsEntityMapper;

    @Override
    public RawFundamentals save(RawFundamentals fundamentals) {
        if (fundamentals == null) {
            throw new IllegalArgumentException("RawFundamentals cannot be null");
        }
        if (fundamentals.getStockId() == null) {
            throw new IllegalStateException("RawFundamentals must have a stockId before persistence");
        }
        if (fundamentals.getReferenceDate() == null) {
            throw new IllegalStateException("RawFundamentals must have a referenceDate before persistence");
        }

        StockEntity stock = jpaStockRepository.getReferenceById(fundamentals.getStockId());
        RawFundamentalsEntity entity = rawFundamentalsEntityMapper.toEntity(fundamentals, stock);
        jpaRawFundamentalsRepository.findByStockIdAndReferenceDate(
                        fundamentals.getStockId(),
                        fundamentals.getReferenceDate()
                )
                .ifPresent(existing -> entity.setId(existing.getId()));
        RawFundamentalsEntity saved = jpaRawFundamentalsRepository.save(entity);
        return rawFundamentalsEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<RawFundamentals> findByStockIdAndReferenceDate(Long stockId, OffsetDateTime referenceDate) {
        return jpaRawFundamentalsRepository.findByStockIdAndReferenceDate(stockId, referenceDate)
                .map(rawFundamentalsEntityMapper::toDomain);
    }
}
