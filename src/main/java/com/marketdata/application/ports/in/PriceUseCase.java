package com.marketdata.application.ports.in;

import com.marketdata.domain.model.Price;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceUseCase {

    List<Price> getPrices(String ticker, LocalDateTime from, LocalDateTime to);

    Price getLatestPrice(String ticker);

}
