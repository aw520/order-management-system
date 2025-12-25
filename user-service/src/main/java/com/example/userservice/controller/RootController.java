package com.example.userservice.controller;

import com.example.userservice.request.RoleChangeRequest;
import com.example.userservice.response.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ROOT')")
@RequestMapping("/root")
public class RootController {

    //TODO: change roles for other users
    @PostMapping("/change-role")
    public ResponseEntity<UserProfileResponse> changeRole(@RequestBody RoleChangeRequest request){
        return null;
    }
}
