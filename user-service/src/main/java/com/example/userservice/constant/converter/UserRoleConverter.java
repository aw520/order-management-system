package com.example.userservice.constant.converter;

import com.example.userservice.constant.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<Set<UserRole>, String>{

    @Override
    public String convertToDatabaseColumn(Set<UserRole> roles) {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<UserRole> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Set.of();
        }

        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }
}
