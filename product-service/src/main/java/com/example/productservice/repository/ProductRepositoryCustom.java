package com.example.productservice.repository;

import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryCustom {
    //search list of products based on criteria
    List<Product> search(SearchCriteria criteria);
    //update
    int updateProductQuantity(UUID id, int delta);
}
