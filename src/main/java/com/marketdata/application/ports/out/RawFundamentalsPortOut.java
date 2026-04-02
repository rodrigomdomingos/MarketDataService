package com.marketdata.application.ports.out;

import com.marketdata.domain.model.RawFundamentals;
import java.time.LocalDate;
import java.util.Optional;

public interface RawFundamentalsPortOut {
    RawFundamentals save(RawFundamentals fundamentals);
    Optional<RawFundamentals> findByStockIdAndReferenceDate(Long stockId, LocalDate referenceDate);
}
