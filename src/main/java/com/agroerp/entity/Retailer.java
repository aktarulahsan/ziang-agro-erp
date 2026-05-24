package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "retailers")
@Getter
@Setter
public class Retailer extends BaseEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String retailerCode;
    @Column(nullable = false, length = 160)
    private String retailerName;
    private String ownerName;
    @Column(nullable = false, length = 30)
    private String mobileNumber;
    private String email;
    @Column(length = 500)
    private String address;
    private String marketName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "territory_id")
    private Territory territory;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal openingBalance = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal currentDueBalance = BigDecimal.ZERO;
}

