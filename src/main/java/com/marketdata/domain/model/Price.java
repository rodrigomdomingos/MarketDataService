package com.marketdata.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

public class Price {
    private Long id;
    private Long stockId;
    private OffsetDateTime date;
    private BigDecimal closePrice;
    private Long volume;

    public Price() {
    }

    public Price(Long id, Long stockId, OffsetDateTime date, BigDecimal closePrice, Long volume) {
        this.id = id;
        this.stockId = stockId;
        this.date = date;
        this.closePrice = closePrice;
        this.volume = volume;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(id, price.id) && Objects.equals(stockId, price.stockId) && Objects.equals(date, price.date) && Objects.equals(closePrice, price.closePrice) && Objects.equals(volume, price.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stockId, date, closePrice, volume);
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", date=" + date +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .stockId(this.stockId)
                .date(this.date)
                .closePrice(this.closePrice)
                .volume(this.volume);
    }

    public static class Builder {
        private Long id;
        private Long stockId;
        private OffsetDateTime date;
        private BigDecimal closePrice;
        private Long volume;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder stockId(Long stockId) {
            this.stockId = stockId;
            return this;
        }

        public Builder date(OffsetDateTime date) {
            this.date = date;
            return this;
        }

        public Builder closePrice(BigDecimal closePrice) {
            this.closePrice = closePrice;
            return this;
        }

        public Builder volume(Long volume) {
            this.volume = volume;
            return this;
        }

        public Price build() {
            return new Price(id, stockId, date, closePrice, volume);
        }
    }
}
