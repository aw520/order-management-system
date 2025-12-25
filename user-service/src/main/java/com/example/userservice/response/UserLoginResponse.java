package com.example.userservice.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String userId;
    private Set<String> roles;
}
