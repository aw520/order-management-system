package com.ordersystem.ordermanagementsystem.exception;

public class OrderStatusUpdateFailedException extends BusinessException{
    public OrderStatusUpdateFailedException(String message) {
        super("ORDER_STATUS_UPDATE_FAILED", message);
    }
}
