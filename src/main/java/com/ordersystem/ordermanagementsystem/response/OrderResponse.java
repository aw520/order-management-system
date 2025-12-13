package com.ordersystem.ordermanagementsystem.response;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.ResponseOrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Builder
@Getter
public class OrderResponse {
    private UUID orderId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String currency;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<ResponseOrderItem> items;
}
