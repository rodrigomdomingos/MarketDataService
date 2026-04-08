package com.marketdata.infrastructure.adapters.outbound.persistence.mapper;

import com.marketdata.infrastructure.adapters.outbound.persistence.entity.PriceEntity;
import com.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import com.marketdata.domain.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {
    @Mapping(target = "stockId", source = "stock.id")
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "date", source = "entity.snapshotAt")
    Price toDomain(PriceEntity entity);

    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "snapshotAt", source = "domain.date")
    PriceEntity toEntity(Price domain, StockEntity stock);
}
