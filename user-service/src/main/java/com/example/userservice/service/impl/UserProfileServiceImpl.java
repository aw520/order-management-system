package com.example.userservice.service.impl;

import com.example.userservice.constant.UserRole;
import com.example.userservice.entity.User;
import com.example.userservice.exception.AuthorizationException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.ServiceUtil;
import com.example.userservice.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(UUID userId, UUID targetUserId, UserProfileUpdateRequest request) {
        User actingUser = userRepository.findByUserId(userId).orElseThrow(()-> new UserNotFoundException("User with id: "+userId.toString()+" not found in database"));
        User targetUser = userRepository.findByUserId(targetUserId).orElseThrow(()-> new UserNotFoundException("User with id: "+targetUserId.toString()+" not found in database"));
        //one can only change self profile unless they are admin or root
        if(!actingUser.equals(targetUser)&& (!actingUser.getRoles().contains(UserRole.ADMIN)&& !actingUser.getRoles().contains(UserRole.ROOT))){
            throw new AuthorizationException("You are not authorized to update this user's profile");
        }
        //update profile
        targetUser.setEmail(request.getEmail());
        targetUser.setFirstName(request.getFirstName());
        targetUser.setLastName(request.getLastName());
        targetUser.setAddress(request.getAddress());
        targetUser = userRepository.save(targetUser);

        UserProfileResponse response = ServiceUtil.userToUserProfileResponse(targetUser);
        return response;
    }

    @Override
    public UserProfileResponse getUserProfile(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new UserNotFoundException("User with id: "+userId.toString()+" not found in database"));
        return ServiceUtil.userToUserProfileResponse(user);
    }


}
