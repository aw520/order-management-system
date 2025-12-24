package com.example.userservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum UserRole {
    
    CLIENT("Client", 0), ADMIN("Admin", 1);
    
    private final String name;
    private final Integer dbValue;

    public static UserRole nameToRole(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }

        for (UserRole role: UserRole.values()) {
            if (role.getName().compareToIgnoreCase(name) == 0) {
                return role;
            }
        }
        return null;
    }
    
    public static UserRole intToRole(Integer dbValue) {
        if (dbValue == null) {
            return null;
        }

        for (UserRole role: UserRole.values()) {
            if (Objects.equals(role.getDbValue(), dbValue)) {
                return role;
            }
        }

        return null;
    }
}
