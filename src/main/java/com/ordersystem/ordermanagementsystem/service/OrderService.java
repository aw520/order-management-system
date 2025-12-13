package com.ordersystem.ordermanagementsystem.service;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(OrderCreateRequest orderCreateRequest, UUID userId);
    Order confirmOrder(UUID orderId);
    //Order updateOrderStatus(UUID orderId, OrderStatus newStatus);
    Order updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID userId);
    Order cancelOrder(UUID orderId, UUID userId);
    List<Order> searchOrders(SearchCriteria searchCriteria, UUID userId);

}
