package com.example.orderservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Builder;

@Getter
@AllArgsConstructor
public class ProductOfOrderRequest {
    @NotNull
    private String id;
    @NotNull
    private int quantity;
}
