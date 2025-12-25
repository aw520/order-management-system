package com.example.userservice.service.impl;

import com.example.userservice.exception.InvalidRefreshTokenException;
import com.example.userservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService implements RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    private static final Duration TTL = Duration.ofDays(7);
    private static final String PREFIX = "refresh:";

    @Override
    public String createRefreshToken(UUID userId) {
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                PREFIX + token,
                userId.toString(),
                TTL
        );

        return token;
    }

    @Override
    public UUID validateAndGetUserId(String refreshToken) {
        String userId = redisTemplate.opsForValue()
                .get(PREFIX + refreshToken);

        if (userId == null) {
            throw new InvalidRefreshTokenException("Token is invalid or expired");
        }

        return UUID.fromString(userId);
    }

    @Override
    public void revoke(String refreshToken) {
        redisTemplate.delete(PREFIX + refreshToken);
    }
}

