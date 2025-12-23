package com.example.orderservice.request;

import java.time.ZonedDateTime;

public class ClientOrderSearchRequest {
    private Integer page;
    private Integer size;
    private String orderId;
    private String status;
    private ZonedDateTime createdAfter;
    private ZonedDateTime createdBefore;
    private ZonedDateTime updatedAfter;
    private ZonedDateTime updatedBefore;
    private String sort;
    private boolean descending;
}
