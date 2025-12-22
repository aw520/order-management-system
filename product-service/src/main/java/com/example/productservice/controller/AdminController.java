package com.example.productservice.controller;

import com.example.productservice.request.ProductValidationRequest;
import com.example.productservice.response.GeneralResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @PostMapping("/updateProduct")
    public ResponseEntity<GeneralResponse<String>> updateStock(@RequestBody ProductValidationRequest productValidationRequest){
        //TODO: only manager allow to do this
        //TODO: return if success
        return null;
    }
}
