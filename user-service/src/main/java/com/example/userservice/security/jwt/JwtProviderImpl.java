package com.example.userservice.security.jwt;

import com.example.userservice.constant.UserRole;
import com.example.userservice.security.jwt.JwtProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Component
public class JwtProviderImpl implements JwtProvider {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtProviderImpl(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-expiration-minutes}") long expirationMinutes
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMinutes * 60 * 1000;
    }

    @Override
    public String generateAccessToken(UUID userId, String email, Set<UserRole> roles) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId.toString())                 // sub
                .claim("email", email)
                .claim("roles", roles.stream()
                        .map(Enum::name)
                        .toList())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expirationMillis)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public UUID extractUserId(String token) {
        return UUID.fromString(
                parseClaims(token).getBody().getSubject()
        );
    }

    @Override
    public Set<String> extractRoles(String token) {
        List<String> roles = parseClaims(token)
                .getBody()
                .get("roles", List.class);
        return new HashSet<>(roles);
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}

