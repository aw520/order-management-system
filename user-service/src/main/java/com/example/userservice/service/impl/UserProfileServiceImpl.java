package com.example.userservice.service.impl;

import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.UserProfileService;

import java.util.UUID;

public class UserProfileServiceImpl implements UserProfileService {
    @Override
    public void updateUserPassword(UUID userId, UUID targetUserId, String oldPassword, String newPassword) {

    }

    @Override
    public UserProfileResponse updateUserProfile(UUID userId, UUID targetUserId, UserProfileUpdateRequest request) {
        return null;
    }
}
