package com.marketdata.infrastructure.adapters.outbound.persistence.mapper;

import com.marketdata.domain.model.RawFundamentals;
import com.marketdata.infrastructure.adapters.outbound.persistence.entity.RawFundamentalsEntity;
import com.marketdata.infrastructure.adapters.outbound.persistence.entity.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RawFundamentalsEntityMapper {
    @Mapping(target = "stockId", source = "stock.id")
    RawFundamentals toDomain(RawFundamentalsEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stock", source = "stock")
    RawFundamentalsEntity toEntity(RawFundamentals domain, StockEntity stock);
}
