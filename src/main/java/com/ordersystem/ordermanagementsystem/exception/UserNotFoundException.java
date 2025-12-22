package com.ordersystem.ordermanagementsystem.exception;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException(String identity) {
        super("USER_NOT_FOUND", "User not found: " + identity);
    }
}
