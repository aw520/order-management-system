package com.example.productservice.dto;

import com.example.productservice.constant.Sortable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
