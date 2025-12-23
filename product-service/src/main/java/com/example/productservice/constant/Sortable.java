package com.example.productservice.constant;

public enum Sortable {
    PRODUCT_NAME("productName"),
    PRICE("productPrice"),
    QUANTITY("quantity");

    private final String field;

    Sortable(String field) {
        this.field = field;
    }

    public static Sortable fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Sortable.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public String field() {
        return field;
    }
}


