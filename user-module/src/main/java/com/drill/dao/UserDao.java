package com.drill.dao;

import com.drill.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(Integer id);
}
