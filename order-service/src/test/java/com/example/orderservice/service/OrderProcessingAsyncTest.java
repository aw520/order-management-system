package com.example.orderservice.service;

import com.example.orderservice.config.ProductClient;
import com.example.orderservice.dto.ProductValidationResponse;
import com.example.orderservice.entity.IdempotencyRecord;
import com.example.orderservice.repository.IdempotencyRecordRepository;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.request.ProductOfOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessingAsyncTest {

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderService orderService;

    @Mock
    private IdempotencyRecordRepository idempotencyRecordRepository;

    @InjectMocks
    private OrderProcessingAsync orderProcessingAsync;

    @BeforeEach
    void setUp() {
        //ensure the setter is called
        orderProcessingAsync.setOrderService(orderService);
    }

    @Test
    void validate_success_callsProductServiceAndHandlesResponse() {
        UUID orderId = UUID.randomUUID();

        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .products(List.of(
                        new ProductOfOrderRequest("p1", 2),
                        new ProductOfOrderRequest("p2", 1)
                ))
                .build();

        IdempotencyRecord record = IdempotencyRecord.builder()
                .idempotencyKey(UUID.randomUUID())
                .build();

        when(idempotencyRecordRepository.save(any()))
                .thenReturn(record);

        ProductValidationResponse response = ProductValidationResponse.builder().build();
        when(productClient.validate(any())).thenReturn(response);

        orderProcessingAsync.validate(orderId, request);

        verify(productClient).validate(argThat(req ->
                req.getProducts().size() == 2 &&
                        req.getProducts().get(0).getDelta() < 0
        ));

        verify(orderService).handleValidationResponse(orderId, response);
    }

    @Test
    void validate_productServiceThrows_exceptionIsSwallowed() {
        UUID orderId = UUID.randomUUID();

        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .products(List.of(
                        new ProductOfOrderRequest("p1", 2)
                ))
                .build();

        when(idempotencyRecordRepository.save(any()))
                .thenReturn(IdempotencyRecord.builder()
                        .idempotencyKey(UUID.randomUUID())
                        .build());

        when(productClient.validate(any()))
                .thenThrow(new RuntimeException("service down"));

        assertDoesNotThrow(() ->
                orderProcessingAsync.validate(orderId, request)
        );

        verify(orderService, never()).handleValidationResponse(any(), any());
    }

    @Test
    void validate_quantityIsNegated() {
        UUID orderId = UUID.randomUUID();

        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .products(List.of(
                        new ProductOfOrderRequest("p1", 5)
                ))
                .build();

        when(idempotencyRecordRepository.save(any()))
                .thenReturn(IdempotencyRecord.builder()
                        .idempotencyKey(UUID.randomUUID())
                        .build());

        when(productClient.validate(any()))
                .thenReturn(ProductValidationResponse.builder().build());

        orderProcessingAsync.validate(orderId, request);

        verify(productClient).validate(argThat(req ->
                req.getProducts().get(0).getDelta() == -5
        ));
    }


}

