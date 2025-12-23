package com.example.orderservice.repository.impl;

import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @Override
    public List<Order> searchOrder(SearchCriteria criteria) {
        return List.of();
    }
}
