package com.example.orderservice.controller;

import com.example.orderservice.request.*;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ClientController {

    //TODO: Client can check their own orders
    @GetMapping("/search")
    public ResponseEntity<List<GeneralOrderSearchResponse>> searchOrder(@RequestBody ClientOrderSearchRequest request){
        return null;
    }

    //TODO: Client can search order by id
    @GetMapping("/search/{orderId}")
    public ResponseEntity<OrderResponse> searchOrder(@PathVariable String orderId){
        return null;
    }

    //TODO: Client can place a new order for themself
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request){
        return null;
    }

    //TODO: Client can cancel their own order
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId){
        return null;
    }

}
