package com.example.userservice.response;

import lombok.Builder;

import java.util.Set;

@Builder
public class UserProfileResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private Set<String> roles;
}
