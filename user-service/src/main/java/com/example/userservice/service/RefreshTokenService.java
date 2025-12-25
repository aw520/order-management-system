package com.example.userservice.service;

import java.util.UUID;

public interface RefreshTokenService {

    String createRefreshToken(UUID userId);

    UUID validateAndGetUserId(String refreshToken);

    void revoke(String refreshToken);

    void revokeAll(UUID userId);
}

