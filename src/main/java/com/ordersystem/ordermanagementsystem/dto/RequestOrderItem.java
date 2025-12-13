package com.ordersystem.ordermanagementsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrderItem {

    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}

