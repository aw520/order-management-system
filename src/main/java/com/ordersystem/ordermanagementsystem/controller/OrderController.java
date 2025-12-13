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
    public ResponseEntity<GeneralResponse<OrderResponse>> orderSubmit(@RequestBody @Valid OrderCreateRequest orderCreateRequest,
                                                                      @RequestParam UUID userId) {
        OrderResponse orderResponse = orderService.createOrder(orderCreateRequest, userId);
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
    public ResponseEntity<GeneralResponse<OrderResponse>> cancelOrder(@RequestParam UUID orderId, @RequestParam UUID userId) {
        OrderResponse orderResponse = orderService.cancelOrder(orderId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<OrderResponse>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(orderResponse)
                .build());
    }

    //POST api/orders/search
    @PostMapping("/search")
    public ResponseEntity<GeneralResponse<List<OrderResponse>>> searchOrder(@RequestBody OrderSearchRequest orderSearchRequest, @RequestParam UUID userId) {
        List<OrderResponse> orderResponses = orderService.searchOrders(orderSearchRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<List<OrderResponse>>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(orderResponses)
                .build());
    }

    //POST api/orders/update-status
    @PostMapping("/update-status")
    public ResponseEntity<GeneralResponse<OrderResponse>> updateOrder(@RequestBody OrderUpdateRequest orderUpdateRequest, @RequestParam UUID userId) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderUpdateRequest.getOrderId(), orderUpdateRequest.getNewStatus(), userId);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<OrderResponse>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(orderResponse)
                .build());
    }

    //POST api/orders/{order-id}/confirm
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
