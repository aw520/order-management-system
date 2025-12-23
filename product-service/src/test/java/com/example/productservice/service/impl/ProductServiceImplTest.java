package com.example.productservice.service.impl;

import com.example.productservice.constant.ValidationResult;
import com.example.productservice.dto.ProductUpdateInfo;
import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.entity.IdempotencyRecord;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.InvalidUpdateException;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.repository.IdempotencyRecordRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ProductRepositoryCustom;
import com.example.productservice.request.IndividualProductValidationDTO;
import com.example.productservice.response.ProductValidationResponse;
import com.example.productservice.response.SpecificProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductRepositoryCustom productRepositoryCustom;
    @Mock
    private IdempotencyRecordRepository idempotencyRecordRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private UUID productId;
    private Product product;

    @BeforeEach
    void setup() {
        productId = UUID.randomUUID();
        product = new Product();
        product.setProductId(productId);
        product.setProductName("old");
        product.setProductDescription("old desc");
        product.setImageUrl("old.png");
        product.setQuantity(10);
        product.setProductPrice(new BigDecimal("9.99"));
    }

    // -----------------------
    // updateProduct(...)
    // -----------------------

    @Test
    void updateProduct_shouldUpdateFieldsAndSave() {
        ProductUpdateInfo info = new ProductUpdateInfo();
        info.setId(productId);
        info.setName("new name");
        info.setDescription("new desc");
        info.setImageUrl("new.png");
        info.setQuantity(20);
        info.setPrice(new BigDecimal("19.99"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        SpecificProductResponse resp = productService.updateProduct(info);

        assertThat(resp.getId()).isEqualTo(productId.toString());
        assertThat(resp.getName()).isEqualTo("new name");
        assertThat(resp.getDescription()).isEqualTo("new desc");
        assertThat(resp.getImageUrl()).isEqualTo("new.png");
        assertThat(resp.getQuantity()).isEqualTo(20);
        assertThat(resp.getPrice()).isEqualByComparingTo("19.99");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getProductName()).isEqualTo("new name");
    }

    @Test
    void updateProduct_negativeQuantity_shouldThrow() {
        ProductUpdateInfo info = new ProductUpdateInfo();
        info.setId(productId);
        info.setQuantity(-1);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.updateProduct(info))
                .isInstanceOf(InvalidUpdateException.class)
                .hasMessageContaining("Quantity cannot be negative");

        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_negativePrice_shouldThrow() {
        ProductUpdateInfo info = new ProductUpdateInfo();
        info.setId(productId);
        info.setPrice(new BigDecimal("-0.01"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.updateProduct(info))
                .isInstanceOf(InvalidUpdateException.class)
                .hasMessageContaining("Price cannot be negative");

        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_productNotFound_shouldThrow() {
        ProductUpdateInfo info = new ProductUpdateInfo();
        info.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(info))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository, never()).save(any());
    }

    // -----------------------
    // validateProduct(...)
    // -----------------------

    @Test
    void validateProduct_idempotencyHit_shouldReturnCachedResponse() {
        UUID idempotencyKey = UUID.randomUUID();

        ProductValidationResponse cached = ProductValidationResponse.builder()
                .result(ValidationResult.FULL)
                .products(List.of())
                .build();

        IdempotencyRecord record = IdempotencyRecord.builder()
                .idempotencyKey(idempotencyKey)
                .productValidationResponse(cached)
                .build();

        when(idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(record);

        ProductValidationResponse resp =
                productService.validateProduct(idempotencyKey, List.of());

        assertThat(resp.getResult()).isEqualTo(ValidationResult.FULL);

        verify(productRepository, never()).deductStock(any(), anyInt());
        verify(idempotencyRecordRepository, never()).save(any());
    }

    @Test
    void validateProduct_fullFulfillment_shouldReturnFULL_andSaveIdempotency() {
        UUID idempotencyKey = UUID.randomUUID();

        // NOTE: This assumes IndividualProductValidationDTO has setters.
        IndividualProductValidationDTO req = new IndividualProductValidationDTO();
        req.setId(productId.toString());
        req.setDelta(-3); // place order => decrease by 3

        when(idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(null);
        when(productRepository.deductStock(productId, 3)).thenReturn(1);
        when(productRepository.findByProductId(productId)).thenReturn(Optional.of(product));
        when(idempotencyRecordRepository.save(any(IdempotencyRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductValidationResponse resp =
                productService.validateProduct(idempotencyKey, List.of(req));

        assertThat(resp.getResult()).isEqualTo(ValidationResult.FULL);
        assertThat(resp.getProducts()).hasSize(1);
        assertThat(resp.getProducts().get(0).getRequestedQuantity()).isEqualTo(3);
        assertThat(resp.getProducts().get(0).getDeductedQuantity()).isEqualTo(3);

        verify(idempotencyRecordRepository).save(any(IdempotencyRecord.class));
    }

    @Test
    void validateProduct_deltaNonNegative_shouldThrow() {
        UUID idempotencyKey = UUID.randomUUID();

        IndividualProductValidationDTO req = new IndividualProductValidationDTO();
        req.setId(productId.toString());
        req.setDelta(1); // invalid (cannot increase)

        when(idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(null);

        assertThatThrownBy(() -> productService.validateProduct(idempotencyKey, List.of(req)))
                .isInstanceOf(InvalidUpdateException.class)
                .hasMessageContaining("Cannot increase quantity");

        verify(productRepository, never()).deductStock(any(), anyInt());
        verify(idempotencyRecordRepository, never()).save(any());
    }

    @Test
    void validateProduct_duplicateInsert_shouldReadExistingAndReturn() {
        UUID idempotencyKey = UUID.randomUUID();

        IndividualProductValidationDTO req = new IndividualProductValidationDTO();
        req.setId(productId.toString());
        req.setDelta(-1);

        when(idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(null) // first check: no record
                .thenReturn(IdempotencyRecord.builder() // after save fails: now found
                        .idempotencyKey(idempotencyKey)
                        .productValidationResponse(
                                ProductValidationResponse.builder()
                                        .result(ValidationResult.FULL)
                                        .products(List.of())
                                        .build()
                        )
                        .build());

        when(productRepository.deductStock(productId, 1)).thenReturn(1);
        when(productRepository.findByProductId(productId)).thenReturn(Optional.of(product));

        doThrow(new RuntimeException("duplicate")).when(idempotencyRecordRepository).save(any());

        ProductValidationResponse resp =
                productService.validateProduct(idempotencyKey, List.of(req));

        assertThat(resp.getResult()).isEqualTo(ValidationResult.FULL);
        verify(idempotencyRecordRepository, times(1)).save(any());
    }

    @Test
    void validateProduct_partialFulfillment_shouldReturnPARTIAL() {
        UUID idempotencyKey = UUID.randomUUID();

        IndividualProductValidationDTO req = new IndividualProductValidationDTO();
        req.setId(productId.toString());
        req.setDelta(-5);

        when(idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(null);

        when(productRepository.deductStock(productId, 5)).thenReturn(0);
        when(productRepository.getQuantity(productId)).thenReturn(3);
        when(productRepository.deductStock(productId, 3)).thenReturn(1);
        when(productRepository.findByProductId(productId))
                .thenReturn(Optional.of(product));

        when(idempotencyRecordRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        ProductValidationResponse resp =
                productService.validateProduct(idempotencyKey, List.of(req));

        assertThat(resp.getResult()).isEqualTo(ValidationResult.PARTIAL);

        var item = resp.getProducts().get(0);
        assertThat(item.getRequestedQuantity()).isEqualTo(5);
        assertThat(item.getDeductedQuantity()).isEqualTo(3);

        verify(productRepository).deductStock(productId, 5);
        verify(productRepository).deductStock(productId, 3);
    }

    @Test
    void validateProduct_noneFulfillment_shouldReturnNONE() {
        UUID idempotencyKey = UUID.randomUUID();

        IndividualProductValidationDTO req = new IndividualProductValidationDTO();
        req.setId(productId.toString());
        req.setDelta(-4);

        when(idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(null);

        when(productRepository.deductStock(productId, 4)).thenReturn(0);
        when(productRepository.getQuantity(productId)).thenReturn(0);
        when(productRepository.findByProductId(productId))
                .thenReturn(Optional.of(product));

        when(idempotencyRecordRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        ProductValidationResponse resp =
                productService.validateProduct(idempotencyKey, List.of(req));

        assertThat(resp.getResult()).isEqualTo(ValidationResult.NONE);

        var item = resp.getProducts().get(0);
        assertThat(item.getRequestedQuantity()).isEqualTo(4);
        assertThat(item.getDeductedQuantity()).isEqualTo(0);

        verify(productRepository).deductStock(productId, 4);
        verify(productRepository, never()).deductStock(productId, 0);
    }

    @Test
    void searchProduct_shouldMapProductNameCorrectly() {
        SearchCriteria criteria = new SearchCriteria();

        Product p = new Product();
        p.setProductId(productId);
        p.setProductName("MacBook Pro");
        p.setImageUrl("mac.png");
        p.setQuantity(5);
        p.setProductPrice(new BigDecimal("1999.99"));

        when(productRepositoryCustom.search(criteria))
                .thenReturn(List.of(p));

        var result = productService.searchProduct(criteria);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName())
                .isEqualTo("MacBook Pro");
    }


}