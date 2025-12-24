package com.example.userservice.exception;

public class InvalidPasswordException extends BusinessException{
    public InvalidPasswordException(String message) {
        super("INVALID_PASSWORD_ERROR", message);
    }
}
