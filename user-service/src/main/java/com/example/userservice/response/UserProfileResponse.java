package com.example.userservice.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class UserProfileResponse {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private Set<String> roles;
}
