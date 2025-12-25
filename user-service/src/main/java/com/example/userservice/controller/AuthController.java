package com.example.userservice.controller;

import com.example.userservice.request.RefreshAuthRequest;
import com.example.userservice.request.RegistrationRequest;
import com.example.userservice.request.UserLoginRequest;
import com.example.userservice.response.UserAuthResponse;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //Registration
    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> register(@RequestBody @Valid RegistrationRequest request){
        return ResponseEntity.ok(authService.registration(request));
    }


    //LogIn, JWT issuance
    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody UserLoginRequest request){
        //System.out.println("Login HIT");
        UserAuthResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    // newJWT
    @PostMapping("/refresh")
    public ResponseEntity<UserAuthResponse> refresh(@RequestBody RefreshAuthRequest request){
        UserAuthResponse userAuthResponse = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(userAuthResponse);
    }







}
