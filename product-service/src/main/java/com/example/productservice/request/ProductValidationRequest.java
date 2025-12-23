package com.example.productservice.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductValidationRequest {
    @NotNull
    @NotEmpty
    private List<IndividualProductValidationDTO> products;
    @NotNull
    private String idempotencyKey;
}
