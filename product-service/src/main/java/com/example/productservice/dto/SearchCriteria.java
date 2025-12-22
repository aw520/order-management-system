package com.example.productservice.dto;

import jakarta.validation.constraints.NotNull;

public class SearchCriteria {
    @NotNull
    int page;
    @NotNull
    int size;
    String sort;
    String direction;
    String keyword;
    Boolean inStock;
}
