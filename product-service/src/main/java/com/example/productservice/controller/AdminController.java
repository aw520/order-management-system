package com.example.productservice.controller;

import com.example.productservice.constant.Sortable;
import com.example.productservice.dto.ProductUpdateInfo;
import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.request.UpdateProductRequest;
import com.example.productservice.response.GeneralSearchProductResponse;
import com.example.productservice.response.SpecificProductResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasAnyRole('ADMIN')")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;

    @PostMapping("/updateProduct/{id}")
    public ResponseEntity<SpecificProductResponse> updateProduct(@PathVariable String id, @RequestBody UpdateProductRequest updateProductRequest){
        //only manager allow to do this
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

    @GetMapping
    public ResponseEntity<List<GeneralSearchProductResponse>> searchProduct(@RequestParam(required = false) Integer page,
                                                                            @RequestParam(required = false) Integer size,
                                                                            @RequestParam(required = false) String keyword,
                                                                            @RequestParam(required = false) Boolean inStock,
                                                                            @RequestParam(required = false) String sortBy,
                                                                            @RequestParam(required = false) Boolean sortDirection){
        //give general info about all products have name alike
        SearchCriteria criteria = SearchCriteria.builder()
                .page(page==null?1:page).size(size==null?10:size)
                .sort(Sortable.fromString(sortBy))
                .descending(sortDirection==null||!sortDirection)
                .keyword(keyword)
                .inStock(inStock)
                .build();
        List<GeneralSearchProductResponse> response = productService.searchProduct(criteria);
        return ResponseEntity.ok(response);
    }

}
