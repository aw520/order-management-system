package com.example.productservice.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class GeneralSearchProductResponse {
    /*
    for general, simple information
     */
    private String id;
    private String name;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
}
