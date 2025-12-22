package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductId(UUID productId);

    @Modifying
    @Query("""
        UPDATE Product p
        SET p.quantity = p.quantity - :qty
        WHERE p.productId = :id
          AND p.quantity >= :qty
        """)
    int deductStock(@Param("id") UUID id, @Param("qty") int qty);

    @Query("SELECT p.quantity FROM Product p WHERE p.productId = :id")
    int getQuantity(@Param("id") UUID id);


}
