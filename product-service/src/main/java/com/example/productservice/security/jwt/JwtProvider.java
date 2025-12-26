package com.example.productservice.security.jwt;

import java.util.Set;
import java.util.UUID;

public interface JwtProvider {

    boolean validateToken(String token);

    UUID extractUserId(String token);

    Set<String> extractRoles(String token);
}

