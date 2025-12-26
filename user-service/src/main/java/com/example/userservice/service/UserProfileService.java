package com.example.userservice.service;

import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;

import java.util.Set;
import java.util.UUID;

public interface UserProfileService {
    UserProfileResponse updateUserProfile(UUID userId, UUID targetUserId, UserProfileUpdateRequest request);
    UserProfileResponse getUserProfile(UUID userId);
    UserProfileResponse updateUserRoles(UUID userId, UUID targetUserId, Set<String> role);

}
