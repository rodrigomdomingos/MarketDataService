package com.marketdata.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public class RawFundamentals {
    private Long stockId;
    private LocalDate referenceDate;
    private Double price;
    private Double marketCap;
    private Double pe;
    private Double forwardPe;
    private Double priceToBook;
    private Double roe;
    private Double roa;
    private Double profitMargin;
    private Double dividendYield;
    private Double payoutRatio;
    private Double revenue;
    private Double netIncome;
    private Double debt;
    private Double cash;
    private Double operatingCashflow;
    private Double revenueGrowth;
    private Double earningsGrowth;
    private String source;
    private Double pegRatio;
    private Double freeCashFlow;

    public RawFundamentals(
            Long stockId,
            LocalDate referenceDate,
            Double price,
            Double marketCap,
            Double pe,
            Double forwardPe,
            Double priceToBook,
            Double roe,
            Double roa,
            Double profitMargin,
            Double dividendYield,
            Double payoutRatio,
            Double revenue,
            Double netIncome,
            Double debt,
            Double cash,
            Double operatingCashflow,
            Double revenueGrowth,
            Double earningsGrowth,
            String source,
            Double pegRatio,
            Double freeCashFlow
    ) {
        this.stockId = stockId;
        this.referenceDate = referenceDate;
        this.price = price;
        this.marketCap = marketCap;
        this.pe = pe;
        this.forwardPe = forwardPe;
        this.priceToBook = priceToBook;
        this.roe = roe;
        this.roa = roa;
        this.profitMargin = profitMargin;
        this.dividendYield = dividendYield;
        this.payoutRatio = payoutRatio;
        this.revenue = revenue;
        this.netIncome = netIncome;
        this.debt = debt;
        this.cash = cash;
        this.operatingCashflow = operatingCashflow;
        this.revenueGrowth = revenueGrowth;
        this.earningsGrowth = earningsGrowth;
        this.source = source;
        this.pegRatio = pegRatio;
        this.freeCashFlow = freeCashFlow;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Double getPe() {
        return pe;
    }

    public void setPe(Double pe) {
        this.pe = pe;
    }

    public Double getForwardPe() {
        return forwardPe;
    }

    public void setForwardPe(Double forwardPe) {
        this.forwardPe = forwardPe;
    }

    public Double getPriceToBook() {
        return priceToBook;
    }

    public void setPriceToBook(Double priceToBook) {
        this.priceToBook = priceToBook;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    public Double getRoa() {
        return roa;
    }

    public void setRoa(Double roa) {
        this.roa = roa;
    }

    public Double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(Double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public Double getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(Double dividendYield) {
        this.dividendYield = dividendYield;
    }

    public Double getPayoutRatio() {
        return payoutRatio;
    }

    public void setPayoutRatio(Double payoutRatio) {
        this.payoutRatio = payoutRatio;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(Double netIncome) {
        this.netIncome = netIncome;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Double getOperatingCashflow() {
        return operatingCashflow;
    }

    public void setOperatingCashflow(Double operatingCashflow) {
        this.operatingCashflow = operatingCashflow;
    }

    public Double getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(Double revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    public Double getEarningsGrowth() {
        return earningsGrowth;
    }

    public void setEarningsGrowth(Double earningsGrowth) {
        this.earningsGrowth = earningsGrowth;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getPegRatio() {
        return pegRatio;
    }

    public void setPegRatio(Double pegRatio) {
        this.pegRatio = pegRatio;
    }

    public Double getFreeCashFlow() {
        return freeCashFlow;
    }

    public void setFreeCashFlow(Double freeCashFlow) {
        this.freeCashFlow = freeCashFlow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawFundamentals that = (RawFundamentals) o;
        return Objects.equals(stockId, that.stockId)
                && Objects.equals(referenceDate, that.referenceDate)
                && Objects.equals(price, that.price)
                && Objects.equals(marketCap, that.marketCap)
                && Objects.equals(pe, that.pe)
                && Objects.equals(forwardPe, that.forwardPe)
                && Objects.equals(priceToBook, that.priceToBook)
                && Objects.equals(roe, that.roe)
                && Objects.equals(roa, that.roa)
                && Objects.equals(profitMargin, that.profitMargin)
                && Objects.equals(dividendYield, that.dividendYield)
                && Objects.equals(payoutRatio, that.payoutRatio)
                && Objects.equals(revenue, that.revenue)
                && Objects.equals(netIncome, that.netIncome)
                && Objects.equals(debt, that.debt)
                && Objects.equals(cash, that.cash)
                && Objects.equals(operatingCashflow, that.operatingCashflow)
                && Objects.equals(revenueGrowth, that.revenueGrowth)
                && Objects.equals(earningsGrowth, that.earningsGrowth)
                && Objects.equals(source, that.source)
                && Objects.equals(pegRatio, that.pegRatio)
                && Objects.equals(freeCashFlow, that.freeCashFlow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                stockId,
                referenceDate,
                price,
                marketCap,
                pe,
                forwardPe,
                priceToBook,
                roe,
                roa,
                profitMargin,
                dividendYield,
                payoutRatio,
                revenue,
                netIncome,
                debt,
                cash,
                operatingCashflow,
                revenueGrowth,
                earningsGrowth,
                source,
                pegRatio,
                freeCashFlow
        );
    }

    @Override
    public String toString() {
        return "RawFundamentals{" +
                "stockId=" + stockId +
                ", referenceDate=" + referenceDate +
                ", price=" + price +
                ", marketCap=" + marketCap +
                ", pe=" + pe +
                ", forwardPe=" + forwardPe +
                ", priceToBook=" + priceToBook +
                ", roe=" + roe +
                ", roa=" + roa +
                ", profitMargin=" + profitMargin +
                ", dividendYield=" + dividendYield +
                ", payoutRatio=" + payoutRatio +
                ", revenue=" + revenue +
                ", netIncome=" + netIncome +
                ", debt=" + debt +
                ", cash=" + cash +
                ", operatingCashflow=" + operatingCashflow +
                ", revenueGrowth=" + revenueGrowth +
                ", earningsGrowth=" + earningsGrowth +
                ", source='" + source + '\'' +
                ", pegRatio=" + pegRatio +
                ", freeCashFlow=" + freeCashFlow +
                '}';
    }
}
