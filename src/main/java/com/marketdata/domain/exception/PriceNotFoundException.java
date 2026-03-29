package com.marketdata.domain.exception;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String ticker) {
        super("No price data found for ticker: " + ticker);
    }
}
