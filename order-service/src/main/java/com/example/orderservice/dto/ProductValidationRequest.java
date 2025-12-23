package com.example.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductValidationRequest {
    @NotNull
    @NotEmpty
    private List<IndividualProductValidationDTO> products;
    @NotNull
    private String idempotencyKey;
}
