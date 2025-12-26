package com.example.userservice.controller;

import com.example.userservice.constant.Sortable;
import com.example.userservice.constant.UserRole;
import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.request.UserProfileUpdateRequest;
import com.example.userservice.request.UserSearchRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserProfileService userProfileService;

    //able to check any client's basic profile by userId
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable UUID userId,
                                                              Authentication authentication){
        UserProfileResponse response = userProfileService.getUserProfile(
                UUID.fromString(authentication.getName()),
                userId);
        return ResponseEntity.ok(response);
    }


    //able to search users by email, firstname, lastname, role
    @PostMapping("/search")
    public ResponseEntity<List<UserProfileResponse>> searchUser(@RequestBody UserSearchRequest request, Authentication authentication){
        UserSearchCriteria criteria = UserSearchCriteria.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .sort(Sortable.fieldToSortable(request.getSort()))
                .page(request.getPage()==null?1:request.getPage())
                .size(request.getSize()==null?10:request.getSize())
                .descending(request.getDescending()==null?true:request.getDescending())
                .build();
        if(request.getRoles()!=null&&!request.getRoles().isEmpty()){
            Set<UserRole> roles = request.getRoles().stream().map(role-> UserRole.nameToRole(role)).collect(Collectors.toSet());
            criteria.setRoles(roles);
        }
        List<UserProfileResponse> response = userProfileService.searchUser(criteria, UUID.fromString(authentication.getName()));
        return ResponseEntity.ok(response);
    }

    //able to modify others basic information
    @PostMapping("/{userId}/modify")
    public ResponseEntity<UserProfileResponse> modifyUserProfile(@PathVariable UUID userId,
                                                                 @RequestBody UserProfileUpdateRequest request,
                                                                 Authentication authentication){
        return ResponseEntity.ok(userProfileService.updateUserProfile(
                UUID.fromString(authentication.getName()),
                userId, request));
    }

}
