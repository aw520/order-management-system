package com.example.orderservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductOfOrderRequest {
    @NotNull
    private String id;
    @NotNull
    private int quantity;
}
