package com.example.marketdata.infrastructure.mapper.persistence;

import com.example.marketdata.infrastructure.adapters.outbound.persistence.entity.PriceEntity;
import com.example.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import com.example.marketdata.domain.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {
    @Mapping(target = "stockId", source = "stock.id")
    @Mapping(target = "id", source = "entity.id")
    Price toDomain(PriceEntity entity);

    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "stock", source = "stock")
    PriceEntity toEntity(Price domain, StockEntity stock);
}
