package com.ordersystem.ordermanagementsystem.service;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderCreateRequest orderCreateRequest, Integer userId);
    Order updateOrderStatus(String orderId, OrderStatus newStatus);
    Order updateOrderStatus(String orderId, OrderStatus newStatus, Integer userId);
    List<Order> searchOrders(SearchCriteria searchCriteria);
    List<Order> searchOrders(SearchCriteria searchCriteria, Integer userId);

}
