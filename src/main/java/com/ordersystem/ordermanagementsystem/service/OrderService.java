package com.ordersystem.ordermanagementsystem.service;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.ResponseOrder;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.request.OrderSearchRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    ResponseOrder createOrder(OrderCreateRequest orderCreateRequest, UUID userId);
    ResponseOrder confirmOrder(UUID orderId);
    //Order updateOrderStatus(UUID orderId, OrderStatus newStatus);
    ResponseOrder updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID userId);
    ResponseOrder cancelOrder(UUID orderId, UUID userId);
    List<ResponseOrder> searchOrders(OrderSearchRequest orderSearchRequest, UUID userId);

}
