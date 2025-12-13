package com.ordersystem.ordermanagementsystem.service;

import com.ordersystem.ordermanagementsystem.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    //TODO: User findOrCreateOAuthUser(OAuthUserInfo oauthUserInfo);
    //Integrate OAuth

    User registerLocalUser(String firstName, String lastName, String email, String rawPassword);
    User authenticateLocalUser(String email, String rawPassword);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);

}
