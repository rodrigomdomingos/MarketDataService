package com.marketdata.infrastructure.adapters.outbound.persistence.mapper;

import com.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import com.marketdata.domain.model.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockEntityMapper {
    Stock toDomain(StockEntity entity);
    StockEntity toEntity(Stock domain);
}
