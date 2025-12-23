package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void deductStock_whenEnoughStock_shouldDeductAndReturn1() {
        Product product = new Product();
        //product.setProductId(UUID.randomUUID());
        product.setProductName("Test Product");
        product.setQuantity(10);
        product.setProductPrice(BigDecimal.TEN);

        productRepository.save(product);

        int affected = productRepository.deductStock(
                product.getProductId(), 5);

        assertThat(affected).isEqualTo(1);

        int remaining = productRepository.getQuantity(
                product.getProductId());

        assertThat(remaining).isEqualTo(5);
    }

    @Test
    void deductStock_whenInsufficientStock_shouldReturn0() {
        Product product = new Product();
        //product.setProductId(UUID.randomUUID());
        product.setProductName("Test Product");
        product.setQuantity(3);
        product.setProductPrice(BigDecimal.TEN);

        productRepository.save(product);

        int affected = productRepository.deductStock(
                product.getProductId(), 5);

        assertThat(affected).isEqualTo(0);

        int remaining = productRepository.getQuantity(
                product.getProductId());

        assertThat(remaining).isEqualTo(3);
    }
}

