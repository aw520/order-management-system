package com.ordersystem.ordermanagementsystem.config;

import com.ordersystem.ordermanagementsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        roleRepository.saveIfNotExists("ROLE_ADMIN");
        roleRepository.saveIfNotExists("ROLE_CLIENT");
    }
}
