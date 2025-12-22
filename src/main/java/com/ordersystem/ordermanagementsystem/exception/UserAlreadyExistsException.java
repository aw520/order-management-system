package com.ordersystem.ordermanagementsystem.exception;

public class UserAlreadyExistsException extends BusinessException{
    public UserAlreadyExistsException(String email) {
        super("USER_ALREADY_EXISTS", email + " already exists");
    }
}
