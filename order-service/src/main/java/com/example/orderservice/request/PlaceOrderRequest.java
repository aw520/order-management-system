package com.example.orderservice.request;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
    private List<ProductOfOrderRequest> products;
    private String address;
}

