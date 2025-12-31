package com.example.common.kafka;


import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductValidationResponse {
    private String correlationId;
    private ValidationResult result;
    private List<IndividualProductValidationResponse> products;
    private String orderId;
}
