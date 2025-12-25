package com.example.userservice.entity;

import com.example.userservice.constant.UserRole;
import com.example.userservice.converter.UserRoleConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;


import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", length = 36)
    private UUID userId;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    @NonNull
    @Email
    private String email;
    @NonNull
    private String password;
    private String address;

    @Convert(converter = UserRoleConverter.class)
    @NonNull
    private Set<UserRole> roles;
}

