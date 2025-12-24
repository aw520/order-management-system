package com.example.userservice.controller;

import com.example.userservice.request.PasswordUpdateRequest;
import com.example.userservice.request.RegistrationRequest;
import com.example.userservice.response.UserProfileResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //TODO: Registration
    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> register(@RequestBody @Valid RegistrationRequest request){
        return null;
    }


    //TODO: LogIn, JWT issuance

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
