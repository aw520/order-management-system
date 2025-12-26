package com.example.userservice.controller;

import com.example.userservice.request.RoleChangeRequest;
import com.example.userservice.response.UserProfileResponse;
import com.example.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@PreAuthorize("hasAnyRole('ROOT')")
@RequestMapping("/root")
@RequiredArgsConstructor
public class RootController {

    private final UserProfileService userProfileService;

    //TODO: change roles for other users
    @PostMapping("/change-role")
    public ResponseEntity<UserProfileResponse> changeRole(@RequestBody RoleChangeRequest request, Authentication authentication){
        UserProfileResponse response = userProfileService.updateUserRoles(
                UUID.fromString(authentication.getName()),
                UUID.fromString(request.getTargetUserId()),
                request.getTargetRole()
        );
        return ResponseEntity.ok(response);
    }
}
