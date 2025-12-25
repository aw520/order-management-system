package com.example.userservice.security.jwt;

import com.example.userservice.constant.UserRole;

import java.util.Set;
import java.util.UUID;

public interface JwtProvider {

    String generateAccessToken(
            UUID userId,
            String email,
            Set<UserRole> roles
    );

    boolean validateToken(String token);

    UUID extractUserId(String token);

    Set<String> extractRoles(String token);
}

