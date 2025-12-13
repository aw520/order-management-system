package com.ordersystem.ordermanagementsystem.request;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderUpdateRequest {
    UUID orderId;
    OrderStatus newStatus;
}
