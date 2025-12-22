package com.example.productservice.constant;

public enum Sortable {
    PRODUCT_NAME("productName"),
    PRICE("productPrice"),
    QUANTITY("quantity");

    private final String field;

    Sortable(String field) {
        this.field = field;
    }

    public String field() {
        return field;
    }
}


