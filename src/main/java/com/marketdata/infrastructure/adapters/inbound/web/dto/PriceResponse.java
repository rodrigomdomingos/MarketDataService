package com.marketdata.infrastructure.adapters.inbound.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PriceResponse {
    Long id;
    Long stockId;
    LocalDateTime date;
    BigDecimal closePrice;
    Long volume;
}
