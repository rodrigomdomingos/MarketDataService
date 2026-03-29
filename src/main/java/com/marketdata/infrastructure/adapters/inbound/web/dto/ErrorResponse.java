package com.marketdata.infrastructure.adapters.inbound.web.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {
    String message;
    int status;
    String path;
    OffsetDateTime timestamp;
}
