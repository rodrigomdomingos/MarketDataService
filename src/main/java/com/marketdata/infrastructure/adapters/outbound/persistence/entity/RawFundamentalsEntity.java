package com.marketdata.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "raw_fundamentals", schema = "investments")
public class RawFundamentalsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private StockEntity stock;

    @Column(name = "reference_date", nullable = false)
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

    @Column(nullable = false, length = 50)
    private String source;
}
