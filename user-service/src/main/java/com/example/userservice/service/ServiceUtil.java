package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.response.UserProfileResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class ServiceUtil {

    public static UserProfileResponse userToUserProfileResponse(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return UserProfileResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .roles(roles)
                .build();
    }
}
