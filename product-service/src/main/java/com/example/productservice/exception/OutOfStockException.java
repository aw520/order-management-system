package com.example.productservice.exception;

import java.util.List;

public class OutOfStockException extends BusinessException {
    public OutOfStockException() {
        super("OUT_OF_STOCK", "");
    }

    private static String messageBuilder(List<String> products, List<Integer> quantities) {
        String message = "Not enough stock for products: ";
        for(int i = 0; i < products.size(); i++){
            message += products.get(i) + " (" + quantities.get(i) + ")";
        }
        return message;
    }
}
