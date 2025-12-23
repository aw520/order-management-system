package com.example.orderservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    NEW("New", 0),
    CONFIRMED("Confirmed", 1),
    PARTIAL_CONFIRMED("Partial Confirmed", 2),
    SHIPPED("SHIPPED", 3),
    CANCELLED("CANCELLED", 4);

    private final String value;
    private final Integer dbValue;

    public static OrderStatus getFromValue(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }

        for (OrderStatus status: OrderStatus.values()) {
            if (status.getValue().compareToIgnoreCase(value) == 0) {
                return status;
            }
        }

        return null;
    }

    public static OrderStatus getFromDbValue(Integer dbValue) {
        if (dbValue == null) {
            return null;
        }

        for (OrderStatus status: OrderStatus.values()) {
            if (Objects.equals(status.getDbValue(), dbValue)) {
                return status;
            }
        }

        return null;
    }

    public static boolean canCancel(OrderStatus status) {
        return status == NEW || status == CONFIRMED || status == PARTIAL_CONFIRMED;
    }

    public static List<String> getAll() {
        return Arrays.stream(OrderStatus.values()).map(OrderStatus::getValue).collect(Collectors.toList());
    }
}


