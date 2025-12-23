package com.example.orderservice.response;

import com.example.orderservice.constant.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Setter
@Builder
@Getter
public class GeneralOrderSearchResponse {
    private String orderId;
    private String status;
    private BigDecimal totalPrice;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
