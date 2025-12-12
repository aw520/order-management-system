package com.ordersystem.ordermanagementsystem.repository;

import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<Order, String> {

    // Optional<User> findById(Integer id);

}
