package com.example.productservice.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductUpdateInfo {
    @NotNull
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
}
