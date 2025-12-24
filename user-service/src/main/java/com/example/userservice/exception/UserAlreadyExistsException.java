package com.example.userservice.exception;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException(String message) {
        super("USER_ALREADY_EXISTS_ERROR", message);
    }
}
