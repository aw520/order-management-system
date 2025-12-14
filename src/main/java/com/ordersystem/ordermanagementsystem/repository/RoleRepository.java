package com.ordersystem.ordermanagementsystem.repository;

import com.ordersystem.ordermanagementsystem.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    default Role saveIfNotExists(String roleName) {
        return findByName(roleName)
                .orElseGet(() -> save(Role.builder()
                        .name(roleName)
                        .build()));
    }

}
