package com.example.userservice.dto;

import com.example.userservice.constant.Sortable;
import com.example.userservice.constant.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserSearchCriteria {
    private String email;
    private String firstName;
    private String lastName;
    private Set<UserRole> roles;
    private Sortable sort;
    @Builder.Default
    int page = 1;
    @Builder.Default
    int size = 10;
    @Builder.Default
    private boolean descending = true;
}
