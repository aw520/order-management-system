package com.example.productservice.response;

import lombok.*;

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
}
