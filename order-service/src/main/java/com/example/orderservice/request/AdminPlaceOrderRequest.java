package com.example.orderservice.request;

import java.time.ZonedDateTime;
import java.util.List;

public class AdminPlaceOrderRequest {
    private List<ProductOfOrder> products;
    private String address;
    private String clientId;
    private ZonedDateTime createdAt;
}
