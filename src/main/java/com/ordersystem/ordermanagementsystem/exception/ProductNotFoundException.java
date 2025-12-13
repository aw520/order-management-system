package com.ordersystem.ordermanagementsystem.exception;

public class ProductNotFoundException extends BusinessException{
    public ProductNotFoundException(String productId) {
        super("Product not found: " + productId);
    }
}
