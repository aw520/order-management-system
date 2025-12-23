package com.example.productservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ProductUpdateInfo {
    @NotNull
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;
}
