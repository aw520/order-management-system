package com.example.common.kafka;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IndividualProductValidationResponse {
    private String productId;
    private String productName;
    private String productImageUrl;
    private int requestedQuantity;
    private int deductedQuantity;
    private BigDecimal unitPrice;
}
