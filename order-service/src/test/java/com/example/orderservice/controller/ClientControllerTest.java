package com.example.orderservice.controller;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "CLIENT")
    void searchOrderById_clientOwnOrder_success() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        OrderResponse response = OrderResponse.builder()
                .orderId(orderId.toString())
                .status(OrderStatus.NEW.getValue())
                .build();

        when(orderService.getOrderById(orderId, false, clientId))
                .thenReturn(response);

        mockMvc.perform(get("/orders/search/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()));

        verify(orderService).getOrderById(orderId, false, clientId);
    }

    @Test
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222", roles = "CLIENT")
    void placeOrder_client_success() throws Exception {
        UUID clientId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        OrderResponse response = OrderResponse.builder()
                .orderId(UUID.randomUUID().toString())
                .build();

        when(orderService.placeOrder(eq(clientId), any()))
                .thenReturn(response);

        String requestJson = """
        {
          "address": "client-address",
          "products": []
        }
        """;

        mockMvc.perform(post("/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists());

        verify(orderService).placeOrder(eq(clientId), any(PlaceOrderRequest.class));
    }

    @Test
    @WithMockUser(username = "33333333-3333-3333-3333-333333333333", roles = "CLIENT")
    void cancelOrder_client_success() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        OrderResponse response = OrderResponse.builder()
                .orderId(orderId.toString())
                .status(OrderStatus.CANCELLED.getValue())
                .build();

        when(orderService.cancelOrder(orderId, false, clientId))
                .thenReturn(response);

        mockMvc.perform(post("/orders/cancel/{orderId}", orderId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(orderService).cancelOrder(orderId, false, clientId);
    }

    @Test
    @WithMockUser(username = "44444444-4444-4444-4444-444444444444", roles = "CLIENT")
    void searchOrders_usesAuthenticatedClientId() throws Exception {
        UUID clientId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        when(orderService.searchOrder(any()))
                .thenReturn(List.of());

        String requestJson = """
        {
          "page": 1,
          "size": 10
        }
        """;

        mockMvc.perform(post("/orders/search").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(orderService).searchOrder(argThat(criteria ->
                criteria.getClientId().equals(clientId)
        ));
    }



}

