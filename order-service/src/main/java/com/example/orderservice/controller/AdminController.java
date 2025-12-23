package com.example.orderservice.controller;

import com.example.orderservice.request.AdminOrderSearchRequest;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderSearchResponse;
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
    //TODO: Admin can check anyone's order status
    //TODO: Admin can search order by
    //TODO: Admin can place order for anyone
    //TODO: Admin can cancel order for anyone

    @GetMapping("/searchOrder")
    public ResponseEntity<List<GeneralOrderSearchResponse>> searchOrder(@RequestBody AdminOrderSearchRequest request){
        return null;
    }

    @GetMapping("/searchOrder/{orderId}")
    public ResponseEntity<OrderSearchResponse> searchOrder(@PathVariable String orderId){
        return null;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Void> placeOrder(){}




}
