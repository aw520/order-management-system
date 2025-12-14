package com.ordersystem.ordermanagementsystem.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("Unauthenticated");
        }

        //real runtime (CustomUserDetails)
        if (auth.getPrincipal() instanceof CustomUserDetails cud) {
            return cud.getUserId();
        }

        //test runtime (@WithMockUser)
        if (auth.getPrincipal() instanceof User springUser) {
            return UUID.fromString(springUser.getUsername());
        }

        throw new IllegalStateException("Unsupported principal type");
    }

    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}

