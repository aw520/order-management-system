package com.example.userservice.controller;

import com.example.userservice.request.PasswordUpdateRequest;
import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final AuthService authService;
    private final UserProfileService userProfileService;

    //Everything below require authentication first;
    //password update
    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid PasswordUpdateRequest request,
                                                 Authentication authentication){
        authService.updateUserPassword(UUID.fromString(authentication.getName()),
                request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    //other information update
    @PostMapping("/profile-update")
    public ResponseEntity<UserProfileResponse> updateAccount(@RequestBody @Valid UserProfileUpdateRequest request,
                                                             Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        UserProfileResponse response = userProfileService.updateUserProfile(userId, userId, request);
        return ResponseEntity.ok(response);
    }

    //check self profile
    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication){
        UserProfileResponse response = userProfileService.getUserProfile(UUID.fromString(authentication.getName()));
        return ResponseEntity.ok(response);
    }


}
