package com.marketdata.infrastructure.adapters.inbound.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StockResponse {
    Long id;
    String ticker;
    String exchange;
    String sector;
    String industry;
    String currency;
    Boolean isActive;
    String description;
}
