package com.example.userservice.request;

import lombok.Getter;

import java.util.Set;

@Getter
public class RoleChangeRequest {
    private String targetUserId;
    private Set<String> targetRole;
}
