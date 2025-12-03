package com.ordersystem.dao;

import com.ordersystem.domain.SearchCriteria;
import com.ordersystem.entity.Order;

import java.util.List;

public interface OrderDao {
    String createOrder(Order order);
    String deleteOrder(String id);
    List<Order> searchOrder(SearchCriteria searchCriteria);
    List<Order> searchOrderForUser(SearchCriteria searchCriteria, Integer userId);
    int countOrder(SearchCriteria searchCriteria, Integer userId);
}

