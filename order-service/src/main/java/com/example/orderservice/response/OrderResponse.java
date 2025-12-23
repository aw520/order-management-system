package com.example.orderservice.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderResponse {
    private String orderId;
    private String status;
    private BigDecimal totalPrice;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<ProductOfOrder> products;
}
