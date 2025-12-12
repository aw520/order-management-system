package com.ordersystem.ordermanagementsystem.exception;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}
