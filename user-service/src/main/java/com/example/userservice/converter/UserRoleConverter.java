package com.example.userservice.converter;

import com.example.userservice.constant.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Integer>{

    @Override
    public Integer convertToDatabaseColumn(UserRole role) {
        return role == null ? null : role.getDbValue();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbValue) {
        return UserRole.intToRole(dbValue);
    }
}
