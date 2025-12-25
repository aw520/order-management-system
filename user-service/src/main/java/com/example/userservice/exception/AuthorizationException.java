package com.example.userservice.exception;

public class AuthorizationException extends BusinessException{
    public AuthorizationException(String message) {
        super("AUTHORIZATION_ERROR", message);
    }
}
