package com.example.orderservice.controller;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.exception.handler.GlobalExceptionHandler;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.security.SecurityConfig;
import com.example.orderservice.service.OrderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchOrderById_success() throws Exception {
        UUID orderId = UUID.randomUUID();

        OrderResponse response = OrderResponse.builder()
                .orderId(orderId.toString())
                .status(OrderStatus.NEW.getValue())
                .build();

        when(orderService.getOrderById(eq(orderId), eq(true), isNull()))
                .thenReturn(response);

        mockMvc.perform(get("/admin/orders/search/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value(OrderStatus.NEW.getValue()));

        verify(orderService).getOrderById(orderId, true, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void placeOrder_success() throws Exception {
        UUID clientId = UUID.randomUUID();

        OrderResponse response = OrderResponse.builder()
                .orderId(UUID.randomUUID().toString())
                .build();

        when(orderService.placeOrder(eq(clientId), any()))
                .thenReturn(response);

        String requestJson = """
        {
          "clientId": "%s",
          "address": "test-address",
          "products": []
        }
        """.formatted(clientId);

        mockMvc.perform(post("/admin/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists());

        verify(orderService).placeOrder(eq(clientId), any(PlaceOrderRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrderStatus_success() throws Exception {
        UUID orderId = UUID.randomUUID();

        OrderResponse response = OrderResponse.builder()
                .orderId(orderId.toString())
                .status(OrderStatus.CANCELLED.getValue())
                .build();

        when(orderService.updateOrderStatus(eq(orderId), eq("CANCELLED")))
                .thenReturn(response);

        String requestJson = """
        {
          "newStatus": "CANCELLED"
        }
        """;

        mockMvc.perform(post("/admin/orders/update-status/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(orderService).updateOrderStatus(orderId, "CANCELLED");
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void adminEndpoint_nonAdmin_forbidden() throws Exception {
        mockMvc.perform(get("/admin/orders/search/{orderId}", UUID.randomUUID()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }




}

