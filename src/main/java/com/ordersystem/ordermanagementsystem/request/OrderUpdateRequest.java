package com.ordersystem.ordermanagementsystem.request;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderUpdateRequest {
    UUID orderId;
    OrderStatus newStatus;
}
