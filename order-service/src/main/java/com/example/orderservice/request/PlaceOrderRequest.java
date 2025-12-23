package com.example.orderservice.request;

import java.time.ZonedDateTime;
import java.util.List;

public class PlaceOrderRequest {
    private List<ProductOfOrder> products;
    private String address;
    private ZonedDateTime createdAt;
}

