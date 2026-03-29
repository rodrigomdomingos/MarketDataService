package com.example.marketdata.application.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String ticker) {
        super("Stock not found for ticker: " + ticker);
    }
}
