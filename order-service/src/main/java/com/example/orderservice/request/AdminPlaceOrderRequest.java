package com.example.orderservice.request;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class AdminPlaceOrderRequest {
    private List<ProductOfOrderRequest> products;
    private String address;
    private String clientId;
}
