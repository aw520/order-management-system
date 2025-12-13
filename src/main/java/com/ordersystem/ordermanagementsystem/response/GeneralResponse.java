package com.ordersystem.ordermanagementsystem.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralResponse<T> {
    private ServiceStatus serviceStatus;
    private T data;
}
