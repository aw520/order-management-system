package com.drill.dao;

import com.drill.domain.SearchCriteria;
import com.drill.entity.Order;

import java.util.List;

public interface OrderDao {
    String createOrder(Order order);
    String deleteOrder(String id);
    List<Order> searchOrder(SearchCriteria searchCriteria);
    List<Order> searchOrderForUser(SearchCriteria searchCriteria, Integer userId);
    int countOrder(SearchCriteria searchCriteria, Integer userId);
}

