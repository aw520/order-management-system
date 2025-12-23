package com.example.orderservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
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

    public boolean canUpdateTo(OrderStatus oldStatus, OrderStatus newStatus) {
        HashMap<OrderStatus, HashSet<OrderStatus>> statusTransitionMap = new HashMap<>();
        statusTransitionMap.put(NEW, new HashSet<>(Arrays.asList(CONFIRMED, PARTIAL_CONFIRMED, CANCELLED)));
        statusTransitionMap.put(CONFIRMED, new HashSet<>(Arrays.asList(SHIPPED, CANCELLED)));
        statusTransitionMap.put(PARTIAL_CONFIRMED, new HashSet<>(Arrays.asList(SHIPPED, CANCELLED)));
        return statusTransitionMap.get(oldStatus).contains(newStatus);
    }
}


