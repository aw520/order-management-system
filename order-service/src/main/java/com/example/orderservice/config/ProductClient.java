package com.example.orderservice.config;

import com.example.orderservice.dto.ProductValidationRequest;
import com.example.orderservice.dto.ProductValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        url = "${PRODUCT_SERVICE_URL}",
        configuration = FeignConfig.class
)
public interface ProductClient {

    @PostMapping("/internal/products/validate")
    ProductValidationResponse validate(
            @RequestBody ProductValidationRequest request
    );


}

