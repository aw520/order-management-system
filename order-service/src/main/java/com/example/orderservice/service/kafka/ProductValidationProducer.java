package com.example.orderservice.service.kafka;

import com.example.orderservice.entity.IdempotencyRecord;
import com.example.orderservice.repository.IdempotencyRecordRepository;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.request.ProductOfOrderRequest;
import com.example.common.kafka.IndividualProductValidationDTO;
import com.example.common.kafka.ProductValidationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductValidationProducer {

    private final KafkaTemplate<String, ProductValidationRequest> kafkaTemplate;
    private final IdempotencyRecordRepository idempotencyRecordRepository;

    public void send(UUID orderId, PlaceOrderRequest placeOrderRequest) {
        IdempotencyRecord record = idempotencyRecordRepository.save(IdempotencyRecord.builder()
                .orderId(orderId)
                .createdAt(LocalDateTime.now())
                .build());
        //create request object
        ProductValidationRequest validationRequest = ProductValidationRequest.builder().build();
        validationRequest.setOrderId(orderId.toString());
        validationRequest.setCorrelationId(UUID.randomUUID().toString());
        validationRequest.setIdempotencyKey(record.getIdempotencyKey().toString());
        List<IndividualProductValidationDTO> products = new ArrayList<>();
        for(ProductOfOrderRequest product : placeOrderRequest.getProducts()){
            products.add(IndividualProductValidationDTO.builder()
                    .id(product.getId()).delta(-product.getQuantity()).build());
        }
        validationRequest.setProducts(products);
        log.info("Sending validation request for order {}", orderId);
        kafkaTemplate.send(
                "product.validate.request",
                validationRequest.getOrderId(),
                validationRequest
        );
    }
}

