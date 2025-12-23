package com.example.orderservice.dto;

import com.example.orderservice.constant.OrderStatus;
import lombok.*;
import org.hibernate.boot.model.source.spi.Sortable;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class SearchCriteria {
    @Builder.Default
    int page = 1;
    @Builder.Default
    int size = 10;
    private UUID orderId;
    private OrderStatus status;
    private UUID clientId;
    private ZonedDateTime createdAfter;
    private ZonedDateTime createdBefore;
    private ZonedDateTime updatedAfter;
    private ZonedDateTime updatedBefore;
    private Sortable sort;
    @Builder.Default
    private boolean descending = true;
}

