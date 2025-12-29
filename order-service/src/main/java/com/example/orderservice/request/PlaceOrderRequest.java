package com.example.orderservice.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
public class PlaceOrderRequest {
    private List<ProductOfOrderRequest> products;
    private String address;
}

