package com.example.common.kafka;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductValidationRequest {
    @NotNull
    @NotEmpty
    private List<IndividualProductValidationDTO> products;
    @NotNull
    private String idempotencyKey;
    private String correlationId;
    private String orderId;
}
