package com.example.productservice.service;

import com.example.productservice.dto.ProductUpdateInfo;
import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.request.RequestUpdateProduct;
import com.example.productservice.response.ProductUpdateResponse;
import com.example.productservice.response.ProductValidationResponse;
import com.example.productservice.response.SearchProductListResponse;
import com.example.productservice.response.SearchProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductUpdateResponse updateProduct(ProductUpdateInfo productUpdateInfo);
    ProductValidationResponse validateProduct(List<RequestUpdateProduct> products);
    SearchProductListResponse searchProduct(SearchCriteria criteria);
    SearchProductResponse searchProductById(UUID id);
}
