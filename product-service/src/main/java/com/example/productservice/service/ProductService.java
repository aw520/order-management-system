package com.example.productservice.service;

import com.example.productservice.dto.ProductUpdateInfo;
import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.response.GeneralSearchProductResponse;
import com.example.productservice.response.SpecificProductResponse;
import com.example.common.kafka.IndividualProductValidationDTO;
import com.example.common.kafka.ProductValidationResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    SpecificProductResponse updateProduct(ProductUpdateInfo productUpdateInfo);
    ProductValidationResponse validateProduct(UUID idempotencyKey, List<IndividualProductValidationDTO> products, UUID correlationId, UUID orderId);
    List<GeneralSearchProductResponse> searchProduct(SearchCriteria criteria);
    SpecificProductResponse searchProductById(UUID id);
}
