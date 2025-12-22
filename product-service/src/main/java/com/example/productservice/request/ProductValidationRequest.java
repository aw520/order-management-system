package com.example.productservice.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProductValidationRequest {
    @NotNull
    @NotEmpty
    private List<RequestUpdateProduct> products;
    @NotNull
    private String idempotencyKey;
}
