package com.ordersystem.ordermanagementsystem.repository.impl;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.entity.User;
import com.ordersystem.ordermanagementsystem.repository.OrderRepository;
import com.ordersystem.ordermanagementsystem.repository.OrderRepositoryCustom;
import com.ordersystem.ordermanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(OrderRepositoryCustomImpl.class)
class OrderRepositoryCustomTest {

    @Autowired
    private OrderRepositoryCustom orderRepositoryCustom;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void searchOrder_byStatusAndUser_returnsOnlyMatchingOrders() {
        // given
        User user = userRepository.save(
                User.builder().email("a@b.com").build()
        );

        Order o1 = orderRepository.save(
                Order.builder()
                        .orderStatus(OrderStatus.NEW)
                        .user(user)
                        .build()
        );

        Order o2 = orderRepository.save(
                Order.builder()
                        .orderStatus(OrderStatus.CONFIRMED)
                        .user(user)
                        .build()
        );

        SearchCriteria criteria = SearchCriteria.builder()
                .status(OrderStatus.NEW)
                .pageNumber(1)
                .pageSize(10)
                .build();

        // when
        List<Order> result =
                orderRepositoryCustom.searchOrder(criteria, user.getUserId());

        // then
        assertEquals(1, result.size());
        assertEquals(o1.getOrderId(), result.get(0).getOrderId());
    }


    @Test
    void searchOrder_byOrderId_returnsExactMatch() {
        // given
        User user = userRepository.save(
                User.builder().email("uuid@test.com").build()
        );

        Order o1 = orderRepository.save(
                Order.builder()
                        .orderStatus(OrderStatus.NEW)
                        .user(user)
                        .build()
        );

        Order o2 = orderRepository.save(
                Order.builder()
                        .orderStatus(OrderStatus.NEW)
                        .user(user)
                        .build()
        );

        SearchCriteria criteria = SearchCriteria.builder()
                .orderId(o1.getOrderId())
                .pageNumber(1)
                .pageSize(10)
                .build();

        // when
        List<Order> result =
                orderRepositoryCustom.searchOrder(criteria, user.getUserId());

        // then
        assertEquals(1, result.size());
        assertEquals(o1.getOrderId(), result.get(0).getOrderId());
    }

}

