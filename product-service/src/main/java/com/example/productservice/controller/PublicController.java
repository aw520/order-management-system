package com.example.productservice.controller;

import com.example.productservice.response.GeneralResponse;
import com.example.productservice.response.SearchProductListResponse;
import com.example.productservice.response.SearchProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class PublicController {

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<SearchProductResponse>> getProductById(@PathVariable String id){
        //TODO: can directly from the frontend
        //TODO: give all info about this specific product
        return null;
    }

    @GetMapping
    public ResponseEntity<GeneralResponse<SearchProductListResponse>> searchProduct(@RequestParam int page,
                                                                                    @RequestParam int size,
                                                                                    @RequestParam(required = false) String name,
                                                                                    @RequestParam(required = false) Boolean inStock,
                                                                                    @RequestParam(required = false) String sortBy,
                                                                                    @RequestParam(required = false) Integer sortDirection){
        //TODO: can directly from the frontend
        //TODO: give general info about all products have name alike
        return null;
    }
}

