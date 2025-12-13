package com.ordersystem.ordermanagementsystem.exception;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException(String identity) {
        super("User not found: " + identity);
    }
}
