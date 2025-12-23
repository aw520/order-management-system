package com.example.productservice.controller;

import com.example.productservice.request.ProductValidationRequest;
import com.example.productservice.response.GeneralResponse;
import com.example.productservice.response.GeneralSearchProductResponse;
import com.example.productservice.response.ProductValidationResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class InternalController {
    private final ProductService productService;

    @PostMapping("/validate")
    public ResponseEntity<ProductValidationResponse> validateProductStock(
            @RequestBody ProductValidationRequest productValidationRequest
    ){
        //return no matter what
        ProductValidationResponse responses = productService.validateProduct(
                UUID.fromString(productValidationRequest.getIdempotencyKey()),
                productValidationRequest.getProducts());
        return ResponseEntity.ok(responses);
    }


}
