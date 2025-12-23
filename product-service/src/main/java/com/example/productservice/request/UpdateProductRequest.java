package com.example.productservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateProductRequest {
    private String name;
    private String description;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
}
