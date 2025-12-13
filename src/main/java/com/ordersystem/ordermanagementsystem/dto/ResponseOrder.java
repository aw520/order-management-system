package com.ordersystem.ordermanagementsystem.dto;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import lombok.Builder;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Builder
public class ResponseOrder {
    private UUID orderId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String currency;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<ResponseOrderItem> items;
}
