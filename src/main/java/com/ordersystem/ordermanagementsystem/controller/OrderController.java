package com.ordersystem.ordermanagementsystem.controller;

import com.ordersystem.ordermanagementsystem.dto.ResponseOrder;
import com.ordersystem.ordermanagementsystem.entity.Order;
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
    public ResponseEntity<GeneralResponse<ResponseOrder>> orderSubmit(@RequestBody @Valid OrderCreateRequest orderCreateRequest,
                                                                      @RequestParam UUID userId) {
        ResponseOrder responseOrder = orderService.createOrder(orderCreateRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                GeneralResponse.<ResponseOrder>builder()
                        .serviceStatus(ServiceStatus.builder()
                                .success(true)
                                .build())
                        .data(responseOrder).build()
        );

    }

    //POST api/order/cancel
    @PostMapping("/cancel")
    public ResponseEntity<GeneralResponse<ResponseOrder>> cancelOrder(@RequestParam UUID orderId, @RequestParam UUID userId) {
        ResponseOrder responseOrder = orderService.cancelOrder(orderId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<ResponseOrder>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(responseOrder)
                .build());
    }

    //POST api/orders/search
    @PostMapping("/search")
    public ResponseEntity<GeneralResponse<List<ResponseOrder>>> searchOrder(@RequestBody OrderSearchRequest orderSearchRequest, @RequestParam UUID userId) {
        List<ResponseOrder> responseOrders = orderService.searchOrders(orderSearchRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<List<ResponseOrder>>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(responseOrders)
                .build());
    }

    //POST api/orders/update-status
    @PostMapping("/update-status")
    public ResponseEntity<GeneralResponse<ResponseOrder>> updateOrder(@RequestBody OrderUpdateRequest orderUpdateRequest, @RequestParam UUID userId) {
        ResponseOrder responseOrder = orderService.updateOrderStatus(orderUpdateRequest.getOrderId(), orderUpdateRequest.getNewStatus(), userId);
        return ResponseEntity.status(HttpStatus.OK).body(GeneralResponse.<ResponseOrder>builder()
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .data(responseOrder)
                .build());
    }

}
