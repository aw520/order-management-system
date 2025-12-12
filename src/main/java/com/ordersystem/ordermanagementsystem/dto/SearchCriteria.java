package com.ordersystem.ordermanagementsystem.dto;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class SearchCriteria {
    private int pageNumber;
    private int pageSize;
    private String orderIdLike;
    private OrderStatus status;

    public SearchCriteria() {
        this.pageNumber = 1;
        this.pageSize = 10;
    }

    public String getOrderIdLike() {
        return orderIdLike;
    }

    public void setOrderIdLike(String orderIdLike) {
        this.orderIdLike = orderIdLike;
    }
}
