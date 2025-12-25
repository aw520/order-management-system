package com.example.userservice.service.impl;

import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.UserProfileService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Override
    public UserProfileResponse updateUserProfile(UUID userId, UUID targetUserId, UserProfileUpdateRequest request) {
        return null;
    }

    @Override
    public UserProfileResponse getUserProfile(UUID userId) {
        return null;
    }
}
