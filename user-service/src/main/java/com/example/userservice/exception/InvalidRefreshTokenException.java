package com.example.userservice.exception;

public class InvalidRefreshTokenException extends AuthenticationException{
    public InvalidRefreshTokenException(String message) {
        super("INVALID_REFRESH_TOKEN_ERROR", message);
    }
}
