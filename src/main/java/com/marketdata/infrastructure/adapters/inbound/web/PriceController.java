package com.marketdata.infrastructure.adapters.inbound.web;

import com.marketdata.infrastructure.adapters.inbound.web.dto.PriceResponse;
import com.marketdata.infrastructure.adapters.inbound.web.mapper.WebMapper;
import com.marketdata.application.ports.in.PriceUseCase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/market-data/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceUseCase priceUseCase;
    private final WebMapper webMapper;

    @GetMapping("/{ticker}")
    public List<PriceResponse> getPrices(
            @PathVariable String ticker,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime to
    ) {
        return priceUseCase.getPrices(ticker, from, to).stream()
                .map(webMapper::toResponse)
                .toList();
    }

    @GetMapping("/{ticker}/latest")
    public ResponseEntity<PriceResponse> getLatestPrice(@PathVariable String ticker) {
        return ResponseEntity.ok(webMapper.toResponse(priceUseCase.getLatestPrice(ticker)));
    }
}
