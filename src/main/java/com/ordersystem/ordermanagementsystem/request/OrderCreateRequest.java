package com.ordersystem.ordermanagementsystem.request;

import com.ordersystem.ordermanagementsystem.dto.RequestOrderItem;
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
    List<RequestOrderItem> orderItems;

    private String currency;
}
