package com.ordersystem.ordermanagementsystem.exception;

public class UserAlreadyExistsException extends BusinessException{
    public UserAlreadyExistsException(String email) {
        super(email + " already exists");
    }
}
