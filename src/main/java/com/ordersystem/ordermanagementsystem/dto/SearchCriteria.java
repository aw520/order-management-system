package com.ordersystem.ordermanagementsystem.dto;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class SearchCriteria {
    private int pageNumber;
    private int pageSize;
    private UUID orderId;
    private OrderStatus status;

    public SearchCriteria() {
        this.pageNumber = 1;
        this.pageSize = 10;
    }

}
