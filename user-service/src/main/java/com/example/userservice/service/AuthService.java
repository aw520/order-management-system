package com.example.userservice.service;

import com.example.userservice.request.RegistrationRequest;
import com.example.userservice.request.UserLoginRequest;
import com.example.userservice.response.UserLoginResponse;
import com.example.userservice.response.UserProfileResponse;

public interface AuthService {
    UserProfileResponse registration(RegistrationRequest request);
    UserLoginResponse login(String email, String password);
}
