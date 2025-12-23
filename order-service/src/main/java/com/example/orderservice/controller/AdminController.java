package com.example.orderservice.controller;

import com.example.orderservice.request.AdminOrderSearchRequest;
import com.example.orderservice.request.AdminOrderStatusUpdateRequest;
import com.example.orderservice.request.AdminPlaceOrderRequest;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminController {

    //TODO: Admin can search anyone's order
    @GetMapping("/search")
    public ResponseEntity<List<GeneralOrderSearchResponse>> searchOrder(@RequestBody AdminOrderSearchRequest request){
        return null;
    }

    //TODO: Admin can search order by id
    @GetMapping("/search/{orderId}")
    public ResponseEntity<OrderResponse> searchOrderById(@PathVariable String orderId){
        return null;
    }

    //TODO: Admin can place a new order for anyone
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody AdminPlaceOrderRequest request){
        return null;
    }

    //TODO: Admin can update order status for anyone
    @PostMapping("/update-status/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId, @RequestBody AdminOrderStatusUpdateRequest request){
        return null;
    }



}
