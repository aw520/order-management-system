package com.example.userservice.service;

import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserProfileService {
    UserProfileResponse updateUserProfile(UUID actUserId, UUID targetUserId, UserProfileUpdateRequest request);
    UserProfileResponse getUserProfile(UUID actUserId, UUID targetUserId);
    UserProfileResponse updateUserRoles(UUID actUserId, UUID targetUserId, Set<String> role);
    List<UserProfileResponse> searchUser(UserSearchCriteria criteria, UUID actUserId);

}
