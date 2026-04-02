package com.marketdata.application.ports.in;

import com.marketdata.domain.model.Price;
import java.time.OffsetDateTime;
import java.util.List;

public interface PriceUseCase {

    List<Price> getPrices(String ticker, OffsetDateTime from, OffsetDateTime to);

    Price getLatestPrice(String ticker);

}
