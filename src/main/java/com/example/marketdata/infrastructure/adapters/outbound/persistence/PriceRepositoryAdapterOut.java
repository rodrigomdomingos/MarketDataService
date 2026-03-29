package com.example.marketdata.infrastructure.adapters.outbound.persistence;

import com.example.marketdata.infrastructure.adapters.outbound.persistence.entity.PriceEntity;
import com.example.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import com.example.marketdata.infrastructure.mapper.persistence.PriceEntityMapper;
import com.example.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaPriceRepository;
import com.example.marketdata.infrastructure.adapters.outbound.persistence.repository.JpaStockRepository;
import com.example.marketdata.domain.model.Price;
import com.example.marketdata.application.ports.out.PriceRepositoryPortOut;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapterOut implements PriceRepositoryPortOut {
    private final JpaPriceRepository jpaPriceRepository;
    private final JpaStockRepository jpaStockRepository;
    private final PriceEntityMapper priceEntityMapper;

    @Override
    public List<Price> findByStockAndDateRange(String ticker, LocalDate from, LocalDate to) {
        return jpaPriceRepository.findByStock_TickerIgnoreCaseAndDateBetweenOrderByDateAsc(ticker, from, to)
                .stream()
                .map(priceEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Price> findLatestByStock(String ticker) {
        return jpaPriceRepository.findFirstByStock_TickerIgnoreCaseOrderByDateDesc(ticker)
                .map(priceEntityMapper::toDomain);
    }

    @Override
    public void saveAll(List<Price> prices) {
        if (prices == null || prices.isEmpty()) {
            return;
        }
        List<PriceEntity> entities = prices.stream()
                .map(price -> {
                    StockEntity stock = jpaStockRepository.getReferenceById(price.getStockId());
                    return priceEntityMapper.toEntity(price, stock);
                })
                .toList();
        jpaPriceRepository.saveAll(entities);
    }
}
