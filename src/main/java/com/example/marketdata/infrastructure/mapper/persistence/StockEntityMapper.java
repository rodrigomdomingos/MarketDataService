package com.example.marketdata.infrastructure.mapper.persistence;

import com.example.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import com.example.marketdata.domain.model.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockEntityMapper {
    Stock toDomain(StockEntity entity);
    StockEntity toEntity(Stock domain);
}
