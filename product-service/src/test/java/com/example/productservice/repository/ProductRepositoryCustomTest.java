package com.example.productservice.repository;

import com.example.productservice.constant.Sortable;
import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.impl.ProductRepositoryCustomImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ProductRepositoryCustomImpl.class)
class ProductRepositoryCustomTest {

    @Autowired
    private ProductRepositoryCustomImpl productRepositoryCustom;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void search_withKeyword_shouldReturnMatchingProducts() {
        Product p1 = new Product();
        p1.setProductName("Apple MacBook Pro");
        p1.setQuantity(5);
        p1.setProductPrice(BigDecimal.TEN);

        Product p2 = new Product();
        p2.setProductName("Dell Laptop");
        p2.setQuantity(5);
        p2.setProductPrice(BigDecimal.TEN);

        productRepository.saveAll(List.of(p1, p2));

        SearchCriteria criteria = SearchCriteria.builder()
                .keyword("Apple")
                .page(1)
                .size(10)
                .build();

        List<Product> result = productRepositoryCustom.search(criteria);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName())
                .containsIgnoringCase("Apple");
    }

    @Test
    void search_withInStockTrue_shouldExcludeZeroQuantity() {
        Product inStock = new Product();
        inStock.setProductName("In Stock");
        inStock.setQuantity(3);
        inStock.setProductPrice(BigDecimal.TEN);

        Product outOfStock = new Product();
        outOfStock.setProductName("Out of Stock");
        outOfStock.setQuantity(0);
        outOfStock.setProductPrice(BigDecimal.TEN);

        productRepository.saveAll(List.of(inStock, outOfStock));

        SearchCriteria criteria = SearchCriteria.builder()
                .inStock(true)
                .page(1)
                .size(10)
                .build();

        List<Product> result = productRepositoryCustom.search(criteria);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isGreaterThan(0);
    }

    @Test
    void search_withSortingAsc_shouldOrderByPrice() {
        Product cheap = new Product();
        cheap.setProductName("Cheap");
        cheap.setQuantity(5);
        cheap.setProductPrice(BigDecimal.ONE);

        Product expensive = new Product();
        expensive.setProductName("Expensive");
        expensive.setQuantity(5);
        expensive.setProductPrice(BigDecimal.TEN);

        productRepository.saveAll(List.of(expensive, cheap));

        SearchCriteria criteria = SearchCriteria.builder()
                .sort(Sortable.PRICE)
                .direction(1) // ascending
                .page(1)
                .size(10)
                .build();

        List<Product> result = productRepositoryCustom.search(criteria);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductPrice())
                .isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void updateProductQuantity_shouldUpdateAndReturnNewQuantity() {
        Product product = new Product();
        product.setProductName("Update Qty");
        product.setQuantity(5);
        product.setProductPrice(BigDecimal.TEN);

        Product saved = productRepository.save(product);

        int updatedQuantity = productRepositoryCustom
                .updateProductQuantity(saved.getProductId(), -2);

        assertThat(updatedQuantity).isEqualTo(3);

        Product refreshed = productRepository.findById(saved.getProductId()).orElseThrow();
        assertThat(refreshed.getQuantity()).isEqualTo(3);
    }
}
