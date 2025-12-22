package com.ordersystem.ordermanagementsystem.exception;

public class InvalidPasswordException extends BusinessException{
    public InvalidPasswordException(String message) {
        super("INVALID_PASSWORD", message);
    }
}
