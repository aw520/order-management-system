package com.example.userservice.exception;

public class InvalidEmailException extends BusinessException{
    public InvalidEmailException(String email) {
        super("INVALID_EMAIL_ERROR", email + "is not valid email address");
    }
}
