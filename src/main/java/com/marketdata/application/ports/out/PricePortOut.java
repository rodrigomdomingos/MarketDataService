package com.marketdata.application.ports.out;

import com.marketdata.domain.model.Price;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface PricePortOut {
    List<Price> findByStockAndDateRange(String ticker, OffsetDateTime from, OffsetDateTime to);
    Optional<Price> findLatestByStock(String ticker);
    Optional<Price> findByStockIdAndDate(Long stockId, OffsetDateTime date);
    void saveAll(List<Price> prices);
    void save(Price price);
}
