package com.ordersystem.ordermanagementsystem.exception;

public class InvalidEmailException extends BusinessException{
    public InvalidEmailException(String email) {
        super(email + " is not a valid email");
    }
}
