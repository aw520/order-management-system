package com.drill;

import com.drill.dao.OrderDao;
import com.drill.dao.impl.OrderDaoImpl;
import com.drill.domain.SearchCriteria;
import com.drill.entity.Order;

import java.time.ZonedDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) {
        OrderDao orderDao = new OrderDaoImpl();

        Order order = getSampleOrder();

        String orderId = orderDao.createOrder(order);
        System.out.println("Created Order ID: " + orderId);

        List<Order> orders = orderDao.searchOrder(new SearchCriteria());
        System.out.println("Orders Retrieved: " + orders);

        int orderCount = orderDao.countOrder(new SearchCriteria(), null);
        System.out.println("Total Order Count: " + orderCount);

        List<Order> investorOrders = orderDao.searchOrderForUser(new SearchCriteria(), 2);
        System.out.println("Investor Orders: " + investorOrders);

        String deletedOrderId = orderDao.deleteOrder(orderId);
        System.out.println("Order Deleted: " + deletedOrderId);
    }

    private static Order getSampleOrder() {
        return Order.builder()
                .orderId("ZACK_ORDER004")
                .clOrderId("CL004")
                .orderStatus(1)
                .orderQuantity("300")
                .side(0)
                .orderType(1)
                .price("250.50")
                .priceType(1)
                .currency("EUR")
                .instrumentName("TSLA")
                .settleType(2)
                .settleDate("2025-01-15")
                .tradeDate("2025-01-10")
                .creationTime(ZonedDateTime.now())
                .interestedParty("Investor Party")
                .userId(2)
                .build();
    }
}
