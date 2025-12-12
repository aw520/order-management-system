package com.ordersystem.ordermanagementsystem.service;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);
    Order updateOrderStatus(String orderId, OrderStatus newStatus);
    Order updateOrderStatus(String orderId, OrderStatus newStatus, Integer userId);
    List<Order> searchOrders(SearchCriteria searchCriteria);
    List<Order> searchOrders(SearchCriteria searchCriteria, Integer userId);

}
