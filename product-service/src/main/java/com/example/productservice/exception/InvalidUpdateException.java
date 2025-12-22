package com.example.productservice.exception;

public class InvalidUpdateException extends BusinessException{
    public InvalidUpdateException(String message) {
        super("INVALID_UPDATE", message);
    }
}
