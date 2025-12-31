package com.example.productservice.service;

import com.example.common.kafka.ProductValidationRequest;
import com.example.common.kafka.ProductValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductValidationListener {

    private final KafkaTemplate<String, ProductValidationResponse> kafkaTemplate;
    private final ProductService productService;

    @KafkaListener(
            topics = "product.validate.request",
            groupId = "product-service"
    )
    public void onValidationRequest(ProductValidationRequest request) {

        log.info("Received validation request: {}", request);
        try{// call validation logic
            ProductValidationResponse response = productService.validateProduct(
                    UUID.fromString(request.getIdempotencyKey()),
                    request.getProducts(),
                    UUID.fromString(request.getCorrelationId()),
                    UUID.fromString(request.getOrderId()));
            log.info("Sending validation response: {}", response);

            kafkaTemplate.send(
                    "product.validate.response",
                    request.getOrderId(),
                    response
            );
        }catch (Exception e){
            //TODO: update response format
            /*
            kafkaTemplate.send(
                    "product.validate.response",
                    request.getOrderId(),
                    e.getMessage()
            );
             */
        }




    }
}
