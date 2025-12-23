package com.example.orderservice.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class ProductOfOrderResponse {
    private String id;
    private String name;
    private String imageUrl;
    private int purchasedQuantity;
    private int fulfilledQuantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
