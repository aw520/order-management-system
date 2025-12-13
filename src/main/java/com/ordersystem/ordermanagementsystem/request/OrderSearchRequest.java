package com.ordersystem.ordermanagementsystem.request;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class OrderSearchRequest {
    private int pageNumber;
    private int pageSize;
    private String orderIdLike;
    private Integer status;
}

