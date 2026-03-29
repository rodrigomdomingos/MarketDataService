package com.example.marketdata.application.ports.in;

import com.example.marketdata.domain.model.Price;
import java.time.LocalDate;
import java.util.List;

public interface PriceUseCase {
    List<Price> getPrices(String ticker, LocalDate from, LocalDate to);
    Price getLatestPrice(String ticker);
}
