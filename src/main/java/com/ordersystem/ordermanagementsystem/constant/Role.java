package com.ordersystem.ordermanagementsystem.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER, Admin;

    public static Role roleOf(String roleName) {
        if (roleName == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        try {
            return Role.valueOf(roleName.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role: " + roleName);
        }
    }
}
