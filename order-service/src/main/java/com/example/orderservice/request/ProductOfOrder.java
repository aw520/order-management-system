package com.example.orderservice.request;

import jakarta.validation.constraints.NotNull;

public class ProductOfOrder {
    @NotNull
    private String id;
    @NotNull
    private int quantity;
}
