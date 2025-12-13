package com.ordersystem.ordermanagementsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class ResponseOrderItem {
    @NotNull
    private String productName;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;
}
