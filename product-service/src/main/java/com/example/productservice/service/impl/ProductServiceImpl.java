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
import com.example.productservice.response.*;
import com.example.productservice.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final IdempotencyRecordRepository idempotencyRecordRepository;

    @Override
    @Transactional
    public SpecificProductResponse updateProduct(ProductUpdateInfo productUpdateInfo) {
        UUID id = productUpdateInfo.getId();
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id.toString()));
        if(product!=null){
            if(productUpdateInfo.getName()!=null){
                product.setProductName(productUpdateInfo.getName());
            }
            if(productUpdateInfo.getDescription()!=null){
                product.setProductDescription(productUpdateInfo.getDescription());
            }
            if(productUpdateInfo.getImageUrl()!=null){
                product.setImageUrl(productUpdateInfo.getImageUrl());
            }
            if(productUpdateInfo.getQuantity()!=null){
                if(productUpdateInfo.getQuantity()<0){
                    throw new InvalidUpdateException("Quantity cannot be negative");
                }else{
                    product.setQuantity(productUpdateInfo.getQuantity());
                }
            }
            if(productUpdateInfo.getPrice()!=null){
                if(productUpdateInfo.getPrice().compareTo(new BigDecimal(0))<0){
                    throw new InvalidUpdateException("Price cannot be negative");
                }else{
                    product.setProductPrice(productUpdateInfo.getPrice());
                }
            }
        }
        return productToSpecificProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductValidationResponse validateProduct(
            UUID idempotencyKey,
            List<IndividualProductValidationDTO> products
    ) {

        // idempotency check
        IdempotencyRecord existing = idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey);
        if (existing != null) {
            return existing.getProductValidationResponse();
        }

        List<IndividualProductValidationResponse> responses = new ArrayList<>();
        boolean anyDeducted = false;
        boolean allFullyFulfilled = true;

        for (IndividualProductValidationDTO request : products) {

            if (request.getDelta() >= 0) {
                throw new InvalidUpdateException("Cannot increase quantity when placing order");
            }

            UUID productId = UUID.fromString(request.getId());
            int requested = Math.abs(request.getDelta());

            // try full deduction atomically
            int affected = productRepository.deductStock(productId, requested);

            int deducted;
            if (affected == 1) {
                // fully fulfilled
                deducted = requested;
            } else {
                // not enough stock → partial or none
                int remaining = productRepository.getQuantity(productId);
                if (remaining > 0) {
                    productRepository.deductStock(productId, remaining);
                    deducted = remaining;
                    allFullyFulfilled = false;
                } else {
                    deducted = 0;
                    allFullyFulfilled = false;
                }
            }

            if (deducted > 0) {
                anyDeducted = true;
            }

            Product product = productRepository.findByProductId(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId.toString()));

            responses.add(
                    IndividualProductValidationResponse.builder()
                            .productId(product.getProductId().toString())
                            .productName(product.getProductName())
                            .productImageUrl(product.getImageUrl())
                            .requestedQuantity(requested)
                            .deductedQuantity(deducted)
                            .build()
            );
        }

        ValidationResult result;
        if (!anyDeducted) {
            result = ValidationResult.NONE;
        } else if (allFullyFulfilled) {
            result = ValidationResult.FULL;
        } else {
            result = ValidationResult.PARTIAL;
        }

        ProductValidationResponse response = ProductValidationResponse.builder()
                .result(result)
                .products(responses)
                .build();

        // persist idempotency record
        IdempotencyRecord record = IdempotencyRecord.builder()
                .idempotencyKey(idempotencyKey)
                .productValidationResponse(response)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            idempotencyRecordRepository.save(record);
        } catch (Exception e) {
            // concurrent duplicate insert → read existing
            IdempotencyRecord saved =
                    idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey);
            if (saved != null) {
                return saved.getProductValidationResponse();
            }
            throw e;
        }

        return response;
    }


    @Override
    public List<GeneralSearchProductResponse> searchProduct(SearchCriteria criteria) {
        List<Product> products = productRepositoryCustom.search(criteria);
        List<GeneralSearchProductResponse> generalSearchProductResponses = products
                .stream().map(this::productToGeneralSearchProductResponse)
                .toList();
        return generalSearchProductResponses;
    }

    @Override
    public SpecificProductResponse searchProductById(UUID id) {
        Product product = productRepository.findByProductId(id).orElseThrow(()-> new ProductNotFoundException(id.toString()));
        return productToSpecificProductResponse(product);
    }

    private SpecificProductResponse productToSpecificProductResponse(Product product){
        return SpecificProductResponse.builder()
                .id(product.getProductId().toString())
                .name(product.getProductName())
                .imageUrl(product.getImageUrl())
                .quantity(product.getQuantity())
                .price(product.getProductPrice())
                .description(product.getProductDescription())
                .build();
    }

    private GeneralSearchProductResponse productToGeneralSearchProductResponse(Product product){
        return GeneralSearchProductResponse.builder()
                .id(product.getProductId().toString())
                .name(product.getProductName())
                .imageUrl(product.getImageUrl())
                .quantity(product.getQuantity())
                .price(product.getProductPrice())
                .build();
    }
}
