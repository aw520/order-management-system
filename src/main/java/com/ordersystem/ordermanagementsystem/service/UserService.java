package com.ordersystem.ordermanagementsystem.service;

import com.ordersystem.ordermanagementsystem.entity.User;
import com.ordersystem.ordermanagementsystem.response.LoginResponse;
import com.ordersystem.ordermanagementsystem.response.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    //TODO: User findOrCreateOAuthUser(OAuthUserInfo oauthUserInfo);
    //Integrate OAuth

    UserResponse registerLocalUser(String firstName, String lastName, String email, String rawPassword);
    LoginResponse authenticateLocalUser(String email, String rawPassword);
    UserResponse findByEmail(String email);
    UserResponse findById(UUID id);

}
