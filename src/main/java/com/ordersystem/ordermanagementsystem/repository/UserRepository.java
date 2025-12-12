package com.ordersystem.ordermanagementsystem.repository;

import com.ordersystem.ordermanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Optional<User> findById(Integer id);

}
