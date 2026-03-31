package com.marketdata.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "stock", schema = "investments")
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticker;

    private String exchange;
    private String sector;
    private String industry;
    private String currency;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(length = 2000)
    private String description;

    @Column(name = "historical_data_loaded", nullable = false, columnDefinition = "boolean default false")
    private boolean historicalDataLoaded = false;
}
