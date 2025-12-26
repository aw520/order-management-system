package com.example.userservice.request;

import lombok.Getter;

import java.util.Set;

@Getter
public class UserSearchRequest {
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
    private Integer page;
    private Integer size;
    private String sort;
    private Boolean descending;
}
