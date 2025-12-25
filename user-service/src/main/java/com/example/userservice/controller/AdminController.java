package com.example.userservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    //TODO: able to check anyone's basic profile
    //TODO: able to search users by email, firstname, lastname

}
