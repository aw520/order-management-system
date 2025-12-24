package com.example.orderservice.repository.impl;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.constant.Sortable;
import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(OrderRepositoryCustomImpl.class)
class OrderRepositoryCustomImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderRepositoryCustom orderRepositoryCustom;

    private Order persistOrder(
            UUID clientId,
            OrderStatus status,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {
        Order order = Order.builder()
                .clientId(clientId)
                .orderStatus(status)
                .creationTime(createdAt)
                .lastUpdateTime(updatedAt)
                .totalPrice(BigDecimal.valueOf(100))
                .build();
        em.persist(order);
        return order;
    }

    @Test
    void searchOrder_byClientId() {
        UUID clientId = UUID.randomUUID();

        persistOrder(clientId, OrderStatus.NEW,
                ZonedDateTime.now().minusDays(1),
                ZonedDateTime.now());

        persistOrder(UUID.randomUUID(), OrderStatus.NEW,
                ZonedDateTime.now().minusDays(1),
                ZonedDateTime.now());

        SearchCriteria criteria = SearchCriteria.builder()
                .clientId(clientId)
                .page(1)
                .size(10)
                .build();

        List<Order> result = orderRepositoryCustom.searchOrder(criteria);

        assertEquals(1, result.size());
        assertEquals(clientId, result.get(0).getClientId());
    }

    @Test
    void searchOrder_byStatus() {
        persistOrder(UUID.randomUUID(), OrderStatus.NEW,
                ZonedDateTime.now().minusDays(1),
                ZonedDateTime.now());

        persistOrder(UUID.randomUUID(), OrderStatus.CANCELLED,
                ZonedDateTime.now().minusDays(1),
                ZonedDateTime.now());

        SearchCriteria criteria = SearchCriteria.builder()
                .status(OrderStatus.CANCELLED)
                .page(1)
                .size(10)
                .build();

        List<Order> result = orderRepositoryCustom.searchOrder(criteria);

        assertEquals(1, result.size());
        assertEquals(OrderStatus.CANCELLED, result.get(0).getOrderStatus());
    }

    @Test
    void searchOrder_createdAfter() {
        ZonedDateTime cutoff = ZonedDateTime.now().minusDays(2);

        persistOrder(UUID.randomUUID(), OrderStatus.NEW,
                ZonedDateTime.now().minusDays(1),
                ZonedDateTime.now());

        persistOrder(UUID.randomUUID(), OrderStatus.NEW,
                ZonedDateTime.now().minusDays(5),
                ZonedDateTime.now());

        SearchCriteria criteria = SearchCriteria.builder()
                .createdAfter(cutoff)
                .page(1)
                .size(10)
                .build();

        List<Order> result = orderRepositoryCustom.searchOrder(criteria);

        assertEquals(1, result.size());
    }


    @Test
    void searchOrder_pagination() {
        UUID clientId = UUID.randomUUID();

        for (int i = 0; i < 5; i++) {
            persistOrder(clientId, OrderStatus.NEW,
                    ZonedDateTime.now().minusDays(i),
                    ZonedDateTime.now());
        }

        SearchCriteria criteria = SearchCriteria.builder()
                .clientId(clientId)
                .page(1)
                .size(2)
                .build();

        List<Order> result = orderRepositoryCustom.searchOrder(criteria);

        assertEquals(2, result.size());
    }

    @Test
    void searchOrder_sortByCreationTimeDesc() {
        UUID clientId = UUID.randomUUID();

        Order older = persistOrder(clientId, OrderStatus.NEW,
                ZonedDateTime.now().minusDays(3),
                ZonedDateTime.now());

        Order newer = persistOrder(clientId, OrderStatus.NEW,
                ZonedDateTime.now().minusDays(1),
                ZonedDateTime.now());

        SearchCriteria criteria = SearchCriteria.builder()
                .clientId(clientId)
                .sort(Sortable.CREATION_TIME)
                .descending(true)
                .page(1)
                .size(10)
                .build();

        List<Order> result = orderRepositoryCustom.searchOrder(criteria);

        assertEquals(newer.getOrderId(), result.get(0).getOrderId());
    }



}

