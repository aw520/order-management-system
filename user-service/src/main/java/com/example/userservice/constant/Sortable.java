package com.example.userservice.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sortable {
    USER_ID("userId"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email");

    private final String field;

    public static Sortable fieldToSortable(String field) {
        if (field == null) {
            return null;
        }
        field = field.trim();
        for (Sortable sortable : Sortable.values()) {
            if (sortable.getField().equalsIgnoreCase(field)) {
                return sortable;
            }
        }
        return null;
    }
}
