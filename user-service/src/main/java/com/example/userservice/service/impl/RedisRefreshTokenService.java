package com.example.userservice.service.impl;

import com.example.userservice.exception.InvalidRefreshTokenException;
import com.example.userservice.exception.ReusedTokenException;
import com.example.userservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService implements RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    private static final Duration TTL = Duration.ofDays(7);
    private static final Duration REUSE_TTL = Duration.ofMinutes(5);

    private static final String PREFIX = "refresh:";
    private static final String USED_PREFIX = "used_refresh:";
    private static final String USER_PREFIX = "user:";


    @Override
    public String createRefreshToken(UUID userId) {
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                PREFIX + token,
                userId.toString(),
                TTL
        );

        //track ownership
        redisTemplate.opsForSet().add(
                USER_PREFIX + userId + ":tokens",
                token
        );

        redisTemplate.expire(USER_PREFIX + userId + ":tokens", TTL);


        return token;
    }

    @Override
    public UUID validateAndGetUserId(String refreshToken) {

        //if reused
        if(redisTemplate.hasKey(USED_PREFIX + refreshToken)){
            String userId = redisTemplate.opsForValue().get(USED_PREFIX + refreshToken);
            throw new ReusedTokenException("Reused refresh token detected", UUID.fromString(userId));
        }

        String userId = redisTemplate.opsForValue()
                .get(PREFIX + refreshToken);

        //if invalid(simply garbage)
        if (userId == null) {
            throw new InvalidRefreshTokenException("Token is invalid");
        }

        return UUID.fromString(userId);
    }

    @Override
    public void revoke(String refreshToken) {

        String refreshKey = PREFIX + refreshToken;
        String userId = redisTemplate.opsForValue().get(refreshKey);

        if (userId != null) {
            redisTemplate.delete(refreshKey);
            redisTemplate.opsForSet().remove(
                    USER_PREFIX + userId + ":tokens",
                    refreshToken
            );

            redisTemplate.opsForValue().set(
                    USED_PREFIX + refreshToken,
                    userId,
                    REUSE_TTL
            );
        }
    }

    @Override
    public void revokeAll(UUID userId) {

        String userTokensKey = USER_PREFIX + userId + ":tokens";

        Set<String> tokens = redisTemplate.opsForSet().members(userTokensKey);

        if (tokens != null) {
            for (String token : tokens) {
                redisTemplate.delete(PREFIX + token);
            }
        }

        // Remove the user's token set itself
        redisTemplate.delete(userTokensKey);
    }



}

