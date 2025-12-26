package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.response.UserAuthResponse;
import com.example.userservice.response.UserProfileResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class ServiceUtil {

    public static UserProfileResponse userToUserProfileResponseComplete(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        //complete profile
        UserProfileResponse response = UserProfileResponse.builder()
                .userId(user.getUserId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .roles(roles)
                .build();
        return response;
    }

    public static UserProfileResponse userToUserProfileResponseBasic(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        //basic profile
        UserProfileResponse response = UserProfileResponse.builder()
                .userId(user.getUserId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(roles)
                .build();
        return response;
    }

    public static UserAuthResponse userToUserAuthResponse(User user, String accessToken, String refreshToken) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        return UserAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId().toString())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

}
