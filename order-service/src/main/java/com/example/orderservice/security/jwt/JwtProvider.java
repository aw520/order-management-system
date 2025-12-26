package com.example.orderservice.security.jwt;

import com.example.userservice.constant.UserRole;

import java.util.Set;
import java.util.UUID;

public interface JwtProvider {

    boolean validateToken(String token);

    UUID extractUserId(String token);

    Set<String> extractRoles(String token);
}

