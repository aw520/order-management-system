package com.ordersystem.ordermanagementsystem.service;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.response.OrderResponse;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.request.OrderSearchRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(OrderCreateRequest orderCreateRequest, UUID userId);
    OrderResponse confirmOrder(UUID orderId);
    //Order updateOrderStatus(UUID orderId, OrderStatus newStatus);
    OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID userId);
    OrderResponse cancelOrder(UUID orderId, UUID userId);
    List<OrderResponse> searchOrders(OrderSearchRequest orderSearchRequest, UUID userId);

}
