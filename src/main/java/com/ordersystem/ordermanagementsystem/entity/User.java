package com.ordersystem.ordermanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import com.ordersystem.ordermanagementsystem.constant.Role;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
