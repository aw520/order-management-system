package com.example.productservice.controller;

import com.example.productservice.request.ProductValidationRequest;
import com.example.productservice.response.GeneralResponse;
import com.example.productservice.response.ProductValidationResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class InternalController {
    private final ProductService productService;

    @PostMapping("/validate")
    public ResponseEntity<GeneralResponse<ProductValidationResponse>> validateProductStock(
            @RequestBody ProductValidationRequest productValidationRequest
    ){
        //TODO: return for completed or partially completed
        return null;
    }


}
