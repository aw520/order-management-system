package com.example.userservice.controller;

import com.example.userservice.request.PasswordUpdateRequest;
import com.example.userservice.response.UserProfileResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    //Everything below require authentication first;
    //TODO: password update
    @PostMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid PasswordUpdateRequest request){
        return null;
    }

    //TODO: other information update
    @PostMapping("/account-update")
    public ResponseEntity<UserProfileResponse> updateAccount(@RequestBody @Valid UserProfileResponse request){
        return null;
    }
    //TODO: password reset
    //TODO: Refresh token
}
