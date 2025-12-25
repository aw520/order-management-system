package com.example.userservice.exception;

public class InvalidCredentialException extends AuthenticationException{

    public InvalidCredentialException(String message) {
        super("INVALID_CREDENTIAL_ERROR", message);
    }
}
