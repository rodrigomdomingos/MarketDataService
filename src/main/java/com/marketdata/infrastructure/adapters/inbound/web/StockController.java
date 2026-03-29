package com.marketdata.infrastructure.adapters.inbound.web;

import com.marketdata.infrastructure.adapters.inbound.web.dto.StockResponse;
import com.marketdata.infrastructure.adapters.inbound.web.mapper.WebMapper;
import com.marketdata.application.ports.in.StockUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/market-data/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockUseCase stockUseCase;
    private final WebMapper webMapper;

    @GetMapping
    public List<StockResponse> getAllStocks() {
        return stockUseCase.getAllStocks().stream()
                .map(webMapper::toResponse)
                .toList();
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<StockResponse> getStockByTicker(@PathVariable String ticker) {
        return stockUseCase.getStockByTicker(ticker)
                .map(webMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
