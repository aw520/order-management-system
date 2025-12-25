package com.example.userservice.exception;

public abstract class AuthenticationException extends RuntimeException {
    private String errorCode = "AUTHENTICATION_ERROR";
    protected AuthenticationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
