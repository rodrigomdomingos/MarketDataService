package com.marketdata.application.ports.out;

import com.marketdata.domain.model.RawFundamentals;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface RawFundamentalsPortOut {
    RawFundamentals save(RawFundamentals fundamentals);
    Optional<RawFundamentals> findByStockIdAndReferenceDate(Long stockId, OffsetDateTime referenceDate);
}
