package com.example.userservice.service;

import com.example.userservice.request.RegistrationRequest;
import com.example.userservice.response.UserAuthResponse;
import com.example.userservice.response.UserProfileResponse;

public interface AuthService {
    UserProfileResponse registration(RegistrationRequest request);
    UserAuthResponse login(String email, String password);
    UserAuthResponse refreshToken(String refreshToken);
}
