package com.ordersystem.dao;

import com.ordersystem.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(Integer id);
}
