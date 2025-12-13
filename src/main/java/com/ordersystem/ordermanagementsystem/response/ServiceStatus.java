package com.ordersystem.ordermanagementsystem.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceStatus {
    private boolean success;
    private String statusCode;
    private String message;
}
