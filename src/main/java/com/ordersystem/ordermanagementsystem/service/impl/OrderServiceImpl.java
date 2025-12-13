package com.ordersystem.ordermanagementsystem.service.impl;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.repository.OrderRepository;
import com.ordersystem.ordermanagementsystem.repository.OrderRepositoryCustom;
import com.ordersystem.ordermanagementsystem.exception.OrderNotFoundException;
import com.ordersystem.ordermanagementsystem.exception.PermissionDeniedException;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ordersystem.ordermanagementsystem.service.OrderService;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;

    //TODO: orderCreation
    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest orderCreateRequest, Integer userId) {

        Order order = new Order();
        return orderRepository.save(order);
    }

    //TODO: orderComfirmation

    @Override
    @Transactional
    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException(orderId);
        }
        order.setOrderStatus(newStatus.getDbValue());
        order.setLastUpdateTime(ZonedDateTime.now());
        return order;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(String orderId, OrderStatus newStatus, Integer userId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException(orderId);
        }
        if(!order.getUser().getUserId().equals(userId)){
            throw new PermissionDeniedException("update order");
        }
        order.setOrderStatus(newStatus.getDbValue());
        order.setLastUpdateTime(ZonedDateTime.now());
        return order;
    }

    @Override
    public List<Order> searchOrders(SearchCriteria searchCriteria) {
        return orderRepositoryCustom.searchOrder(searchCriteria);
    }

    @Override
    public List<Order> searchOrders(SearchCriteria searchCriteria, Integer userId) {
        return orderRepositoryCustom.searchOrder(searchCriteria, userId);
    }
}
