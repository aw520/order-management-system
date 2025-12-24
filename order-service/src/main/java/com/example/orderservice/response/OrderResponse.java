package com.example.orderservice.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;


@Setter
@Getter
@Builder
public class OrderResponse {
    private String orderId;
    private String status;
    private BigDecimal totalPrice;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<ProductOfOrderResponse> products;
}
