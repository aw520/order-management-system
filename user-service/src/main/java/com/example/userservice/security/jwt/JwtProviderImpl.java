package com.example.userservice.security.jwt;

import com.example.userservice.constant.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


@Component
public class JwtProviderImpl implements JwtProvider {

    private final PrivateKey privateKey;
    private final long expirationMillis;

    public JwtProviderImpl(
            @Value("${security.jwt.private-key}") Resource privateKeyResource,
            @Value("${security.jwt.access-token-expiration-minutes}") long expirationMinutes
    ) {
        try {
            this.privateKey = loadPrivateKey(privateKeyResource);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load private key", e);
        }
        this.expirationMillis = expirationMinutes * 60 * 1000;
    }

    @Override
    public String generateAccessToken(UUID userId, String email, Set<UserRole> roles) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .claim("roles", roles.stream().map(Enum::name).toList())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expirationMillis)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private PrivateKey loadPrivateKey(Resource resource) throws Exception {
        String key;
        try (InputStream is = resource.getInputStream()) {
            key = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        key = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}