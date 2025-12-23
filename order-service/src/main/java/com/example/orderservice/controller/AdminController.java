package com.example.orderservice.controller;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.constant.Sortable;
import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.request.AdminOrderSearchRequest;
import com.example.orderservice.request.AdminOrderStatusUpdateRequest;
import com.example.orderservice.request.AdminPlaceOrderRequest;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminController {

    private final OrderService orderService;

    //Admin can search anyone's order
    @GetMapping("/search")
    public ResponseEntity<List<GeneralOrderSearchResponse>> searchOrder(@RequestBody AdminOrderSearchRequest request){
        SearchCriteria criteria = SearchCriteria.builder()
                .page(request.getPage())
                .size(request.getSize())
                .orderId(UUID.fromString(request.getOrderId()))
                .clientId(UUID.fromString(request.getClientId()))
                .status(OrderStatus.getFromValue(request.getStatus()))
                .createdAfter(request.getCreatedAfter())
                .createdBefore(request.getCreatedBefore())
                .updatedAfter(request.getUpdatedAfter())
                .updatedBefore(request.getUpdatedBefore())
                .sort(Sortable.fromString(request.getSort()))
                .descending(request.isDescending())
                .build();
        List<GeneralOrderSearchResponse> responses = orderService.searchOrder(criteria);
        return ResponseEntity.ok(responses);
    }

    //Admin can search order by id
    @GetMapping("/search/{orderId}")
    public ResponseEntity<OrderResponse> searchOrderById(@PathVariable String orderId){
        OrderResponse response = orderService.getOrderById(UUID.fromString(orderId), true, null);
        return ResponseEntity.ok(response);
    }

    //Admin can place a new order for anyone
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody AdminPlaceOrderRequest request){
        PlaceOrderRequest placeOrderRequest = PlaceOrderRequest.builder()
                .products(request.getProducts())
                .address(request.getAddress())
                .build();
        OrderResponse response = orderService.placeOrder(UUID.fromString(request.getClientId()), placeOrderRequest);
        return ResponseEntity.ok(response);
    }

    //Admin can update order status for anyone
    @PostMapping("/update-status/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId, @RequestBody AdminOrderStatusUpdateRequest request){
        OrderResponse response = orderService.updateOrderStatus(UUID.fromString(orderId), request.getNewStatus());
        return ResponseEntity.ok(response);
    }



}
