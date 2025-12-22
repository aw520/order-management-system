package com.example.productservice.repository.impl;

import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @Override
    public List<Product> search(int page, int size, SearchCriteria criteria) {
        return List.of();
    }

    @Override
    public int updateProductQuantity(UUID id, int delta) {
        return 0;
    }
}
