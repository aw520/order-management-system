package com.example.orderservice.service.impl;

import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.service.OrderService;

import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    @Override
    public List<GeneralOrderSearchResponse> searchOrder(SearchCriteria criteria) {
        return List.of();
    }

    @Override
    public OrderResponse placeOrder(UUID clientId, PlaceOrderRequest request) {
        return null;
    }

    @Override
    public OrderResponse updateOrderStatus(UUID orderId, String status) {
        return null;
    }

    @Override
    public OrderResponse getOrderById(UUID orderId) {
        return null;
    }

    @Override
    public OrderResponse cancelOrder(UUID orderId) {
        return null;
    }
}
