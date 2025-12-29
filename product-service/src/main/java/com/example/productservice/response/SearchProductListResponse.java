package com.example.productservice.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SearchProductListResponse {
    private List<GeneralSearchProductResponse> products;
}
