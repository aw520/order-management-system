package com.ordersystem.ordermanagementsystem.controller;

import com.ordersystem.ordermanagementsystem.response.OrderResponse;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.request.OrderSearchRequest;
import com.ordersystem.ordermanagementsystem.request.OrderUpdateRequest;
import com.ordersystem.ordermanagementsystem.response.GeneralResponse;
import com.ordersystem.ordermanagementsystem.response.ServiceStatus;
import com.ordersystem.ordermanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //POST api/submit
    @PostMapping("/submit")
    public ResponseEntity<GeneralResponse<OrderResponse>> orderSubmit(@RequestBody @Valid OrderCreateRequest orderCreateRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                GeneralResponse.<OrderResponse>builder()
                        .serviceStatus(ServiceStatus.builder()
                                .success(true)
                                .build())
                        .data(orderResponse).build()
        );

    }

    //POST api/order/cancel
    @PostMapping("/cancel")
    public ResponseEntity<GeneralResponse<OrderResponse>> cancelOrder(@RequestParam UUID orderId) {
        OrderResponse orderResponse = orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<OrderResponse>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(orderResponse)
                .build());
    }

    //POST api/orders/search
    @PostMapping("/search")
    public ResponseEntity<GeneralResponse<List<OrderResponse>>> searchOrder(@RequestBody OrderSearchRequest orderSearchRequest) {
        List<OrderResponse> orderResponses = orderService.searchOrders(orderSearchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<List<OrderResponse>>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(orderResponses)
                .build());
    }

    //POST api/orders/update-status
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/update-status")
    public ResponseEntity<GeneralResponse<OrderResponse>> updateOrder(@RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderUpdateRequest.getOrderId(), orderUpdateRequest.getNewStatus());
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<OrderResponse>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(orderResponse)
                .build());
    }

    //POST api/orders/{order-id}/confirm
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<GeneralResponse<OrderResponse>> confirmOrder(@PathVariable String orderId) {
        OrderResponse orderResponse = orderService.confirmOrder(UUID.fromString(orderId));
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<OrderResponse>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(orderResponse)
                .build());
    }





}
