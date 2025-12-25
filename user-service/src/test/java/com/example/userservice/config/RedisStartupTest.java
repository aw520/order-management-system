package com.example.userservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class RedisStartupTest {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    RedisStartupTest(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("redis-test", "hello");
        String value = redisTemplate.opsForValue().get("redis-test");
        System.out.println("Redis test value = " + value);
    }
}

