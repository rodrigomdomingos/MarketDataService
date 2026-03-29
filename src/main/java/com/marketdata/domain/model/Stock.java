package com.marketdata.domain.model;


import java.util.Objects;

public class Stock {
    private Long id;
    private String ticker;
    private String exchange;
    private String sector;
    private String industry;
    private String currency;
    private Boolean isActive;
    private String description;

    public Stock(String ticker, String name) {
        this(null, ticker, null, null, null, null, null, null);
    }

    public Stock(Long id, String ticker, String exchange, String sector, String industry, String currency, Boolean isActive, String description) {
        this.id = id;
        this.ticker = ticker;
        this.exchange = exchange;
        this.sector = sector;
        this.industry = industry;
        this.currency = currency;
        this.isActive = isActive;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) && Objects.equals(ticker, stock.ticker) && Objects.equals(exchange, stock.exchange) && Objects.equals(sector, stock.sector) && Objects.equals(industry, stock.industry) && Objects.equals(currency, stock.currency) && Objects.equals(isActive, stock.isActive) && Objects.equals(description, stock.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker, exchange, sector, industry, currency, isActive, description);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", exchange='" + exchange + '\'' +
                ", sector='" + sector + '\'' +
                ", industry='" + industry + '\'' +
                ", currency='" + currency + '\'' +
                ", isActive=" + isActive +
                ", description='" + description + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .ticker(this.ticker)
                .exchange(this.exchange)
                .sector(this.sector)
                .industry(this.industry)
                .currency(this.currency)
                .isActive(this.isActive)
                .description(this.description);
    }

    public static class Builder {
        private Long id;
        private String ticker;
        private String exchange;
        private String sector;
        private String industry;
        private String currency;
        private Boolean isActive;
        private String description;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder ticker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public Builder exchange(String exchange) {
            this.exchange = exchange;
            return this;
        }

        public Builder sector(String sector) {
            this.sector = sector;
            return this;
        }

        public Builder industry(String industry) {
            this.industry = industry;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Stock build() {
            return new Stock(id, ticker, exchange, sector, industry, currency, isActive, description);
        }
    }
}
