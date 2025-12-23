package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualProductValidationResponse {
    private String productId;
    private String productName;
    private String productImageUrl;
    private int requestedQuantity;
    private int deductedQuantity;
    private BigDecimal unitPrice;
}
