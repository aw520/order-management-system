package com.example.productservice.controller;

import com.example.productservice.dto.ProductUpdateInfo;
import com.example.productservice.request.IndividualProductValidationDTO;
import com.example.productservice.request.UpdateProductRequest;
import com.example.productservice.response.GeneralResponse;
import com.example.productservice.response.SpecificProductResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @PostMapping("/updateProduct/{id}")
    public ResponseEntity<SpecificProductResponse> updateProduct(@PathVariable String id, @RequestBody UpdateProductRequest updateProductRequest){
        //TODO: only manager allow to do this
        //return if success
        SpecificProductResponse response = productService.updateProduct(ProductUpdateInfo.builder()
                .id(UUID.fromString(id))
                .name(updateProductRequest.getName())
                .description(updateProductRequest.getDescription())
                .imageUrl(updateProductRequest.getImageUrl())
                .quantity(updateProductRequest.getQuantity())
                .price(updateProductRequest.getPrice())
                .build());
        return ResponseEntity.ok(response);
    }
}
