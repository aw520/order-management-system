package com.ordersystem.ordermanagementsystem.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotEmpty
    List<CreateOrderItemRequest> orderItems;

    private String currency;
}
