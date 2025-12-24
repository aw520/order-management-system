package com.example.userservice.exception;

public abstract class AuthenticationException extends org.springframework.security.core.AuthenticationException {
    private String errorCode = "AUTHENTICATION_ERROR";
    protected AuthenticationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
