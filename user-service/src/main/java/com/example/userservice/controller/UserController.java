package com.example.userservice.controller;

import com.example.userservice.request.PasswordUpdateRequest;
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
    //TODO: password update
    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid PasswordUpdateRequest request,
                                                 Authentication authentication){
        authService.updateUserPassword(UUID.fromString(authentication.getName()),
                request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    //TODO: other information update
    @PostMapping("/account-update")
    public ResponseEntity<UserProfileResponse> updateAccount(@RequestBody @Valid UserProfileResponse request,
                                                             Authentication authentication){
        return null;
    }

    //TODO: check self profile
    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication){
        return null;
    }
}
