package com.ordersystem;

import com.ordersystem.dao.UserDao;
import com.ordersystem.dao.impl.UserDaoImpl;

public class App {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();

        userDao.getUserById(1).ifPresentOrElse(
                user -> System.out.println("Found user with Optional: " + user.getFirstName()),
                () -> System.out.println("User not found with Optional.")
        );
    }
}
