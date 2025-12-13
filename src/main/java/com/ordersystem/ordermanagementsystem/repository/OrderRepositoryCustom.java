package com.ordersystem.ordermanagementsystem.repository;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> searchOrder(SearchCriteria criteria);
    List<Order> searchOrder(SearchCriteria criteria, Integer userId);
    //Order createOrder(Order order);
    //Order updateOrderStatus(String orderId, OrderStatus status);
}
