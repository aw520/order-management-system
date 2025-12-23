package com.example.productservice.controller;

import com.example.productservice.constant.Sortable;
import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.response.GeneralResponse;
import com.example.productservice.response.GeneralSearchProductResponse;
import com.example.productservice.response.SearchProductListResponse;
import com.example.productservice.response.SpecificProductResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class PublicController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<SpecificProductResponse> searchProductById(@PathVariable String id){
        //can directly from the frontend
        //give all info about this specific product
        SpecificProductResponse response = productService.searchProductById(UUID.fromString(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GeneralSearchProductResponse>> searchProduct(@RequestParam int page,
                                                                                    @RequestParam int size,
                                                                                    @RequestParam(required = false) String keyword,
                                                                                    @RequestParam(required = false) Boolean inStock,
                                                                                    @RequestParam(required = false) String sortBy,
                                                                                    @RequestParam(required = false) Integer sortDirection){
        //can directly from the frontend
        //give general info about all products have name alike
        SearchCriteria criteria = SearchCriteria.builder()
                .page(page).size(size)
                .sort(Sortable.fromString(sortBy))
                .direction(sortDirection)
                .keyword(keyword)
                .inStock(inStock)
                .build();
        List<GeneralSearchProductResponse> response = productService.searchProduct(criteria);
        return ResponseEntity.ok(response);
    }
}

