package com.ordersystem.ordermanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.RequestOrderItem;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.request.OrderSearchRequest;
import com.ordersystem.ordermanagementsystem.request.OrderUpdateRequest;
import com.ordersystem.ordermanagementsystem.response.OrderResponse;
import com.ordersystem.ordermanagementsystem.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    //@WithMockUser(username = "testuser")
    void submitOrder_returns201() throws Exception {
        UUID userId = UUID.randomUUID();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .currency("USD")
                .orderItems(List.of(
                        RequestOrderItem.builder()
                                .productId(UUID.randomUUID())
                                .quantity(2)
                                .build()
                ))
                .build();

        when(orderService.createOrder(any(), eq(userId)))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/submit")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serviceStatus.success").value(true));
    }

    private OrderResponse mockOrderResponse() {
        return OrderResponse.builder()
                .orderId(UUID.randomUUID())
                .status(OrderStatus.NEW)
                .currency("USD")
                .totalPrice(BigDecimal.TEN)
                .build();
    }

    @Test
    void cancelOrder_returns200() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(orderService.cancelOrder(orderId, userId))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/cancel")
                        .param("orderId", orderId.toString())
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceStatus.success").value(true));
    }

    @Test
    void searchOrders_returns200() throws Exception {
        UUID userId = UUID.randomUUID();

        OrderSearchRequest request = OrderSearchRequest.builder()
                .pageNumber(0)
                .pageSize(10)
                .build();

        when(orderService.searchOrders(any(), eq(userId)))
                .thenReturn(List.of(mockOrderResponse()));

        mockMvc.perform(post("/api/orders/search")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void updateOrderStatus_returns200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        OrderUpdateRequest request = OrderUpdateRequest.builder()
                .orderId(orderId)
                .newStatus(OrderStatus.SHIPPED)
                .build();

        when(orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED, userId))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/update-status")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void confirmOrder_returns200() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.confirmOrder(orderId))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/{orderId}/confirm", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceStatus.success").value(true));
    }




}
