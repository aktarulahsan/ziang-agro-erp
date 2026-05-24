package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import com.agroerp.enums.DeliveryStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
public class Delivery extends BaseEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String deliveryNumber;
    @Column(nullable = false)
    private LocalDate deliveryDate;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "retailer_id", nullable = false)
    private Retailer retailer;
    private String deliveryAddress;
    private String vehicleDetails;
    private String deliveryPerson;
    private String deliveryContactNo;
    private String routeName;
    @Column(nullable = false, precision = 14, scale = 2)
    private java.math.BigDecimal transportCost = java.math.BigDecimal.ZERO;
    private String remarks;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryItem> items = new ArrayList<>();
}

