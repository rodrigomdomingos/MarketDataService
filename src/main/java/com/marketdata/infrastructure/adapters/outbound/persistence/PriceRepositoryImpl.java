package com.marketdata.infrastructure.adapters.outbound.persistence;

import com.marketdata.infrastructure.adapters.outbound.persistence.entity.PriceEntity;
import com.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import com.marketdata.infrastructure.adapters.outbound.persistence.mapper.PriceEntityMapper;
import com.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaPriceRepository;
import com.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaStockRepository;
import com.marketdata.domain.model.Price;
import com.marketdata.application.ports.out.PriceRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PriceRepositoryImpl implements PriceRepositoryPortOut {

    private final JpaPriceRepository jpaPriceRepository;
    private final JpaStockRepository jpaStockRepository;
    private final PriceEntityMapper priceEntityMapper;

    @Override
    public List<Price> findByStockAndDateRange(String ticker, LocalDateTime from, LocalDateTime to) {
        return jpaPriceRepository
                .findByStock_TickerIgnoreCaseAndDateBetweenOrderByDateAsc(ticker, from, to)
                .stream()
                .map(priceEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Price> findLatestByStock(String ticker) {
        return jpaPriceRepository
                .findFirstByStock_TickerIgnoreCaseOrderByDateDesc(ticker)
                .map(priceEntityMapper::toDomain);
    }

    @Override
    public void saveAll(List<Price> prices) {
        if (prices == null || prices.isEmpty()) {
            return;
        }

        List<PriceEntity> entities = prices.stream()
                .map(this::toEntityWithStock)
                .toList();

        jpaPriceRepository.saveAll(entities);
    }

    @Override
    public void save(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        PriceEntity entity = toEntityWithStock(price);
        jpaPriceRepository.save(entity);
    }

    private PriceEntity toEntityWithStock(Price price) {
        if (price.getStockId() == null) {
            throw new IllegalStateException("Price must have a stockId before persistence");
        }

        StockEntity stock = jpaStockRepository.getReferenceById(price.getStockId());
        return priceEntityMapper.toEntity(price, stock);
    }
}