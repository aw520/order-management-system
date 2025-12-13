package com.ordersystem.ordermanagementsystem.repository;

import com.ordersystem.ordermanagementsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
