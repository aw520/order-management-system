package com.example.productservice.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class SpecificProductResponse {
    private String id;
    private String name;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
    private String description;
}
