package com.ordersystem.ordermanagementsystem.repository;

import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> searchOrder(SearchCriteria criteria);
    List<Order> searchOrderForUser(SearchCriteria criteria, Integer userId);
    int countOrder(SearchCriteria criteria, Integer userId);
}
