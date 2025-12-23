package com.example.orderservice.exception;

public class OrderStatusUpdateException extends BusinessException {
    public OrderStatusUpdateException(String oldStatus, String newStatus) {
        super("ORDER_STATUS_UPDATE_ERROR", "cannot update order status from " + oldStatus + " to " + newStatus);
    }
}
