package com.example.orderservice.request;

import com.example.orderservice.constant.OrderStatus;
import lombok.Builder;
import org.hibernate.boot.model.source.spi.Sortable;

import java.time.ZonedDateTime;
import java.util.UUID;

public class AdminOrderSearchRequest {
    private Integer page;
    private Integer size;
    private String orderId;
    private String status;
    private String clientId;
    private ZonedDateTime createdAfter;
    private ZonedDateTime createdBefore;
    private ZonedDateTime updatedAfter;
    private ZonedDateTime updatedBefore;
    private String sort;
    private boolean descending;
}
