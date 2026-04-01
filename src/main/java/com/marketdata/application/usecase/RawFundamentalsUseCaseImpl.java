package com.marketdata.application.usecase;

import com.marketdata.application.ports.in.RawFundamentalsUseCase;
import com.marketdata.application.ports.out.RawFundamentalsPortOut;
import com.marketdata.application.ports.out.StockPortOut;
import com.marketdata.domain.exception.StockNotFoundException;
import com.marketdata.domain.model.RawFundamentals;
import com.marketdata.domain.model.Stock;
import com.marketdata.infrastructure.adapters.outbound.provider.YahooFinanceFundamentalsClient;
import java.time.LocalDate;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RawFundamentalsUseCaseImpl implements RawFundamentalsUseCase {

    private static final String SOURCE_YAHOO = "YAHOO_FINANCE";

    private final YahooFinanceFundamentalsClient yahooFinanceFundamentalsClient;
    private final StockPortOut stockRepositoryPort;
    private final RawFundamentalsPortOut rawFundamentalsPortOut;

    @Override
    public void syncRawFundamentals(String ticker) {
        log.info("Syncing raw fundamentals for ticker: {}", ticker);

        Stock stock = stockRepositoryPort.findByTicker(ticker)
                .orElseThrow(() -> new StockNotFoundException(ticker));

        RawFundamentals rawFundamentals = yahooFinanceFundamentalsClient.fetchFundamentals(ticker);
        rawFundamentals.setStockId(stock.getId());
        rawFundamentals.setReferenceDate(LocalDate.now(ZoneOffset.UTC));
        rawFundamentals.setSource(SOURCE_YAHOO);

        rawFundamentalsPortOut.save(rawFundamentals);

        log.info("Saved raw fundamentals for ticker: {}", ticker);
    }
}
