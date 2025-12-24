package com.example.orderservice.constant;

import lombok.Getter;

@Getter
public enum Sortable {
    CLIENT_ID("clientId"),
    CLIENT_NAME("clientName"),
    ORDER_ID("orderId"),
    ORDER_STATUS("orderStatus"),
    CREATION_TIME("creationTime"),
    LAST_UPDATE_TIME("lastUpdateTime"),
    TOTAL_PRICE("totalPrice");

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

}
