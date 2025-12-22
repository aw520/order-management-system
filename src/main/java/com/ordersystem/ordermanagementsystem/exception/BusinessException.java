package com.ordersystem.ordermanagementsystem.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private String errorCode = "BUSINESS_ERROR";
    protected BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}