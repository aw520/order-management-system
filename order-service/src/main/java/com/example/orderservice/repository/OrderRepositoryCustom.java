package com.example.orderservice.repository;

import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> searchOrder(SearchCriteria criteria);
}
