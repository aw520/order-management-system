package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductUpdateInfo;
import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.request.RequestUpdateProduct;
import com.example.productservice.response.ProductUpdateResponse;
import com.example.productservice.response.ProductValidationResponse;
import com.example.productservice.response.SearchProductListResponse;
import com.example.productservice.response.SearchProductResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Override
    public ProductUpdateResponse updateProduct(ProductUpdateInfo productUpdateInfo) {
        return null;
    }

    @Override
    public ProductValidationResponse validateProduct(List<RequestUpdateProduct> products) {
        return null;
    }

    @Override
    public SearchProductListResponse searchProduct(SearchCriteria criteria) {
        return null;
    }

    @Override
    public SearchProductResponse searchProductById(UUID id) {
        return null;
    }
}
