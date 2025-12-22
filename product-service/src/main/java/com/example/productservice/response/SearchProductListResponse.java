package com.example.productservice.response;

import lombok.Builder;

import java.util.List;

@Builder
public class SearchProductListResponse {
    private List<GeneralSearchProductResponse> products;
}
