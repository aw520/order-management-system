package com.example.userservice.entity;

import com.example.userservice.constant.UserRole;
import com.example.userservice.constant.converter.UserRoleConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;


import java.sql.Types;
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
    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    @JdbcTypeCode(Types.BINARY)
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

