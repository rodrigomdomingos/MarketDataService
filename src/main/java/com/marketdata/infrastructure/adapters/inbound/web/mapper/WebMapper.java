package com.marketdata.infrastructure.adapters.inbound.web.mapper;

import com.marketdata.infrastructure.adapters.inbound.web.dto.PriceResponse;
import com.marketdata.infrastructure.adapters.inbound.web.dto.StockResponse;
import com.marketdata.domain.model.Price;
import com.marketdata.domain.model.Stock;
import org.springframework.stereotype.Component;

@Component
public class WebMapper {
    public StockResponse toResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .ticker(stock.getTicker())
                .exchange(stock.getExchange())
                .sector(stock.getSector())
                .industry(stock.getIndustry())
                .currency(stock.getCurrency())
                .isActive(stock.getIsActive())
                .description(stock.getDescription())
                .build();
    }

    public PriceResponse toResponse(Price price) {
        return PriceResponse.builder()
                .id(price.getId())
                .stockId(price.getStockId())
                .date(price.getDate())
                .closePrice(price.getClosePrice())
                .volume(price.getVolume())
                .build();
    }
}
