package com.ordersystem.ordermanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordersystem.ordermanagementsystem.config.SecurityConfig;
import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.RequestOrderItem;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.request.OrderSearchRequest;
import com.ordersystem.ordermanagementsystem.request.OrderUpdateRequest;
import com.ordersystem.ordermanagementsystem.response.OrderResponse;
import com.ordersystem.ordermanagementsystem.security.CustomUserDetailsService;
import com.ordersystem.ordermanagementsystem.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;



import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
    void submitOrder_returns201() throws Exception {
        OrderCreateRequest request = OrderCreateRequest.builder()
                .currency("USD")
                .orderItems(List.of(
                        RequestOrderItem.builder()
                                .productId(UUID.randomUUID())
                                .quantity(2)
                                .build()
                ))
                .build();

        when(orderService.createOrder(any()))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/submit")
                        .with(csrf())
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
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
    void cancelOrder_returns200() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.cancelOrder(orderId))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/cancel")
                        .with(csrf())
                        .param("orderId", orderId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceStatus.success").value(true));
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
    void searchOrders_returns200() throws Exception {
        OrderSearchRequest request = OrderSearchRequest.builder()
                .pageNumber(0)
                .pageSize(10)
                .build();

        when(orderService.searchOrders(any()))
                .thenReturn(List.of(mockOrderResponse()));

        mockMvc.perform(post("/api/orders/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
    void updateOrderStatus_returns200() throws Exception {
        UUID orderId = UUID.randomUUID();

        OrderUpdateRequest request = OrderUpdateRequest.builder()
                .orderId(orderId)
                .newStatus(OrderStatus.SHIPPED)
                .build();

        when(orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/update-status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"CLIENT"}
    )
    @Test
    void updateOrderStatus_asClient_returns403() throws Exception {
        UUID orderId = UUID.randomUUID();

        OrderUpdateRequest request = OrderUpdateRequest.builder()
                .orderId(orderId)
                .newStatus(OrderStatus.SHIPPED)
                .build();

        when(orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/update-status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
    void confirmOrder_returns200() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.confirmOrder(orderId))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/{orderId}/confirm", orderId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceStatus.success").value(true));
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"CLIENT"}
    )
    void confirmOrder_asClient_returns403() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.confirmOrder(orderId))
                .thenReturn(mockOrderResponse());

        mockMvc.perform(post("/api/orders/{orderId}/confirm", orderId).with(csrf()))
                .andExpect(status().isForbidden());
    }




}
