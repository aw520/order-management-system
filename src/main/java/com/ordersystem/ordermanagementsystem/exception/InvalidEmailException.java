package com.ordersystem.ordermanagementsystem.exception;

public class InvalidEmailException extends BusinessException{
    public InvalidEmailException(String email) {
        super("INVALID_EMAIL", email + " is not a valid email");
    }
}
