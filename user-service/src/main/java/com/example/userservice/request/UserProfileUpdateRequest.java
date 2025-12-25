package com.example.userservice.request;

import lombok.Getter;

@Getter
public class UserProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
}
