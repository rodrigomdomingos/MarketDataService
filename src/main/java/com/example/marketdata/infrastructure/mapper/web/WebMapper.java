package com.example.marketdata.infrastructure.mapper.web;

import com.example.marketdata.application.dto.PriceResponse;
import com.example.marketdata.application.dto.StockResponse;
import com.example.marketdata.domain.model.Price;
import com.example.marketdata.domain.model.Stock;
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
