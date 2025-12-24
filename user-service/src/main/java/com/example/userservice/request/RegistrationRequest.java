package com.example.userservice.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RegistrationRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
