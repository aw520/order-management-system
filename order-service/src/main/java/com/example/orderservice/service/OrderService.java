package com.example.orderservice.service;

import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<GeneralOrderSearchResponse> searchOrder(SearchCriteria criteria);
    OrderResponse placeOrder(UUID clientId, PlaceOrderRequest request);
    OrderResponse updateOrderStatus(UUID orderId, String status);//admin only
    OrderResponse getOrderById(UUID orderId);//need authorization
    OrderResponse cancelOrder(UUID orderId);//need authorization
}
