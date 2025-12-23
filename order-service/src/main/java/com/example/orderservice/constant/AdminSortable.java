package com.example.orderservice.constant;

public enum AdminSortable {
    USER_ID("userId"),
    USER_NAME("userName"),
    ORDER_ID("orderId"),
    STATUS("status"),
    CREATED_AT("createAt"),
    UPDATED_AT("updateAt"),
    TOTAL_PRICE("totalPrice");

    private final String field;

    AdminSortable(String field) {
        this.field = field;
    }

    public static AdminSortable fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return AdminSortable.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

}
