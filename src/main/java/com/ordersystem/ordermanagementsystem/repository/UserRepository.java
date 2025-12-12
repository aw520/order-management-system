package com.ordersystem.ordermanagementsystem.repository;

import com.ordersystem.ordermanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    // Optional<User> findById(Integer id);

}
