package com.example.userservice.service;

import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;

import java.util.UUID;

public interface UserProfileService {
    void updateUserPassword(UUID userId, UUID targetUserId, String oldPassword, String newPassword);
    UserProfileResponse updateUserProfile(UUID userId, UUID targetUserId, UserProfileUpdateRequest request);


}
