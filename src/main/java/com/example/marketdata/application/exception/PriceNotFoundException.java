package com.example.marketdata.application.exception;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String ticker) {
        super("No price data found for ticker: " + ticker);
    }
}
