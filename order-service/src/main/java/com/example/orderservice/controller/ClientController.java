package com.example.orderservice.controller;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.constant.Sortable;
import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.request.*;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ClientController {

    private final OrderService orderService;

    //Client can check their own orders
    @PostMapping("/search")
    public ResponseEntity<List<GeneralOrderSearchResponse>> searchOrder(@RequestBody ClientOrderSearchRequest request,
                                                                        Authentication authentication){
        UUID clientId = UUID.fromString(authentication.getName());
        SearchCriteria criteria = SearchCriteria.builder()
                .page(request.getPage())
                .size(request.getSize())
                .orderId(request.getOrderId() != null
                        ? UUID.fromString(request.getOrderId())
                        : null)
                .clientId(clientId)
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

    //Client can search order by id
    @GetMapping("/search/{orderId}")
    public ResponseEntity<OrderResponse> searchOrder(@PathVariable("orderId") String orderId, Authentication authentication){
        UUID clientId = UUID.fromString(authentication.getName());
        OrderResponse response = orderService.getOrderById(UUID.fromString(orderId), false, clientId);
        return ResponseEntity.ok(response);
    }

    //Client can place a new order for themself
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request,
                                                    Authentication authentication){
        UUID clientId = UUID.fromString(authentication.getName());
        OrderResponse response = orderService.placeOrder(clientId, request);
        return ResponseEntity.ok(response);
    }

    //Client can cancel their own order
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable("orderId") String orderId,
                                                     Authentication authentication){
        UUID clientId = UUID.fromString(authentication.getName());
        OrderResponse response = orderService.cancelOrder(UUID.fromString(orderId), false, clientId);
        return ResponseEntity.ok(response);
    }


}
