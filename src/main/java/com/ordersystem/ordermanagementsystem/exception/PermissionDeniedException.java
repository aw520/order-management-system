package com.ordersystem.ordermanagementsystem.exception;

public class PermissionDeniedException extends BusinessException{

    public PermissionDeniedException(String message) {
        super("User does not have permission to " + message);
    }
}
