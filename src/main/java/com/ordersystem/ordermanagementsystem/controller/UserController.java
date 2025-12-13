package com.ordersystem.ordermanagementsystem.controller;


import com.ordersystem.ordermanagementsystem.request.UserLoginRequest;
import com.ordersystem.ordermanagementsystem.request.UserRegisterRequest;
import com.ordersystem.ordermanagementsystem.response.GeneralResponse;
import com.ordersystem.ordermanagementsystem.response.LoginResponse;
import com.ordersystem.ordermanagementsystem.response.ServiceStatus;
import com.ordersystem.ordermanagementsystem.response.UserResponse;
import com.ordersystem.ordermanagementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<GeneralResponse<UserResponse>> register(
            @RequestBody @Valid UserRegisterRequest request) {

        UserResponse user = userService.registerLocalUser(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GeneralResponse.<UserResponse>builder()
                        .serviceStatus(ServiceStatus.builder().success(true).build())
                        .data(user)
                        .build());
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<GeneralResponse<LoginResponse>> login(
            @RequestBody @Valid UserLoginRequest request) {

        LoginResponse login = userService.authenticateLocalUser(request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.OK)
                .body(GeneralResponse.<LoginResponse>builder()
                        .serviceStatus(ServiceStatus.builder().success(true).build())
                        .data(login)
                        .build());
    }

    //TODO: User search
}
