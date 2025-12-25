package com.example.userservice.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ReusedTokenException extends AuthenticationException{

    private final UUID userId;

    public ReusedTokenException(String message, UUID userId) {
        super("REUSED_TOKEN_ERROR", message);
        this.userId = userId;
    }
}
