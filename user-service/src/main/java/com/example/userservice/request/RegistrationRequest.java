package com.example.userservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class RegistrationRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    private String firstName;
    private String lastName;
}
