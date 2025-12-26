package com.example.userservice.repository;

import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> searchUser(UserSearchCriteria criteria);
}
