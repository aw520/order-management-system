package com.example.productservice.dto;

import com.example.productservice.constant.Sortable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchCriteria {
    @NotNull
    int page;
    @NotNull
    int size;
    Sortable sort;
    Integer direction;
    String keyword;
    Boolean inStock;
}
