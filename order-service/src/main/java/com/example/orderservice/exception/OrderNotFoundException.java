package com.example.orderservice.exception;

public class OrderNotFoundException extends BusinessException{
    public OrderNotFoundException(String orderId) {
        super("ORDER_NOT_FOUND", orderId + " not found.");
    }
}
