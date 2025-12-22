package com.ordersystem.ordermanagementsystem.exception;

public class OrderConfirmationFailedException extends BusinessException{
    public OrderConfirmationFailedException(String message) {
        super("ORDER_CONFIRMATION_FAILED", message);
    }
}
