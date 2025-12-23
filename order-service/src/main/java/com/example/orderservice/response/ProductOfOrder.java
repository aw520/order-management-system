package com.example.orderservice.response;

import java.math.BigDecimal;

public class ProductOfOrder {
    private String id;
    private String name;
    private String imageUrl;
    private int purchasedQuantity;
    private int fulfilledQuantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
