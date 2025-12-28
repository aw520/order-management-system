package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.IndividualProductValidationDTO;
import com.example.orderservice.dto.ProductValidationRequest;
import com.example.orderservice.dto.ProductValidationResponse;
import com.example.orderservice.entity.IdempotencyRecord;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.IdempotencyRecordRepository;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.request.ProductOfOrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderProcessingAsync {

    private final ProductClient productClient;
    private final IdempotencyRecordRepository idempotencyRecordRepository;

    @Autowired
    public OrderProcessingAsync(ProductClient productClient, IdempotencyRecordRepository idempotencyRecordRepository) {
        this.idempotencyRecordRepository = idempotencyRecordRepository;
        this.productClient = productClient;
    }

    private OrderService orderService;

    @Autowired
    public void setOrderService (@Lazy OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderService getOrderService() {
        return orderService;
    }



    @Async
    @Transactional
    public void validate(UUID orderId, PlaceOrderRequest placeOrderRequest) {

        //save to idempotency table
        IdempotencyRecord record = idempotencyRecordRepository.save(IdempotencyRecord.builder()
                .orderId(orderId)
                .createdAt(LocalDateTime.now())
                .build());
        //create request object
        ProductValidationRequest validationRequest = ProductValidationRequest.builder()
                .idempotencyKey(record.getIdempotencyKey().toString()).build();
        List<IndividualProductValidationDTO> products = new ArrayList<>();
        for(ProductOfOrderRequest product : placeOrderRequest.getProducts()){
            products.add(new IndividualProductValidationDTO(product.getId(), -product.getQuantity()));
        }
        validationRequest.setProducts(products);
        //call product service
        try {
            ProductValidationResponse response = productClient.validate(validationRequest);
            //handle response
            orderService.handleValidationResponse(orderId, response);
        } catch (Exception ex) {//TODO: handle failure
            //orderService.handleValidationFailure( ex);
        }
    }
}

