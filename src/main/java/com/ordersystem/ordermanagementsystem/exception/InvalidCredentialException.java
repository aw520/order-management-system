package com.ordersystem.ordermanagementsystem.exception;

public class InvalidCredentialException extends BusinessException{
    public InvalidCredentialException(String message) {
        super("Invalid "+message);
    }
}
