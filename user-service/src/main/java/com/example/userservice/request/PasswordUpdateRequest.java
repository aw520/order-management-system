package com.example.userservice.request;

import lombok.Getter;

@Getter
public class PasswordUpdateRequest {
    private String oldPassword;
    private String newPassword;
}
