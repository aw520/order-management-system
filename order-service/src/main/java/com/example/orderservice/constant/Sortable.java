package com.example.orderservice.constant;

import lombok.Getter;

@Getter
public enum Sortable {
    CLIENT_ID("client_id"),
    CLIENT_NAME("client_name"),
    ORDER_ID("order_id"),
    ORDER_STATUS("order_status"),
    CREATION_TIME("creation_time"),
    LAST_UPDATE_TIME("last_update_time"),
    TOTAL_PRICE("total_price");

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
