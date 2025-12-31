package com.example.orderservice.service.kafka;

import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import com.example.common.kafka.ProductValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductValidationReplyHandler {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    private final Map<String, CompletableFuture<ProductValidationResponse>> pending =
            new ConcurrentHashMap<>();

    public CompletableFuture<ProductValidationResponse> register(String correlationId) {
        CompletableFuture<ProductValidationResponse> future =
                new CompletableFuture<>();
        pending.put(correlationId, future);
        return future;
    }

    @KafkaListener(
            topics = "product.validate.response",
            groupId = "order-service"
    )
    @Transactional
    public void onReply(ProductValidationResponse response) {
        log.info("Received validation response: {}", response);
        orderService.handleValidationResponse(UUID.fromString(response.getOrderId()), response);
    }
}

