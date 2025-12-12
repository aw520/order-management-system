package com.ordersystem.ordermanagementsystem.exception;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
    }
}
