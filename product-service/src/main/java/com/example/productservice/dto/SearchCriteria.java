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
    @Builder.Default
    int page = 1;
    @Builder.Default
    int size = 10;
    Sortable sort;
    @Builder.Default
    boolean descending = true;
    String keyword;
    Boolean inStock;
}
