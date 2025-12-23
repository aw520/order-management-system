package com.example.orderservice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_products")
public class OrderProduct {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private UUID productId;
    private String imageUrl;
    private String productName;
    private BigDecimal unitPrice;
    private Integer purchasedQuantity;
    private Integer fulfilledQuantity;
    private BigDecimal totalPrice;
}

