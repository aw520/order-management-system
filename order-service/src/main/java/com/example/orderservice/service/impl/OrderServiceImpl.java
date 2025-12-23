package com.example.orderservice.service.impl;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.constant.ValidationResult;
import com.example.orderservice.dto.IndividualProductValidationResponse;
import com.example.orderservice.dto.ProductValidationResponse;
import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderProduct;
import com.example.orderservice.exception.AuthorizationException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.exception.OrderStatusUpdateException;
import com.example.orderservice.exception.ValidationException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderRepositoryCustom;
import com.example.orderservice.request.PlaceOrderRequest;
import com.example.orderservice.request.ProductOfOrderRequest;
import com.example.orderservice.response.GeneralOrderSearchResponse;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.response.ProductOfOrderResponse;
import com.example.orderservice.service.OrderProcessingAsync;
import com.example.orderservice.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryCustom orderRepositoryCustom;
    private final OrderRepository orderRepository;
    private final OrderProcessingAsync orderProcessingAsync;

    @Override
    public List<GeneralOrderSearchResponse> searchOrder(SearchCriteria criteria) {
        List<Order> responses = orderRepositoryCustom.searchOrder(criteria);
        List<GeneralOrderSearchResponse> generalOrderSearchResponses = new ArrayList<>();
        for(Order order : responses){
            GeneralOrderSearchResponse response = GeneralOrderSearchResponse.builder()
                    .orderId(order.getOrderId().toString())
                    .status(order.getOrderStatus().getValue())
                    .totalPrice(order.getTotalPrice())
                    .createdAt(order.getCreationTime())
                    .updatedAt(order.getLastUpdateTime())
                    .build();
            generalOrderSearchResponses.add(response);
        }
        return generalOrderSearchResponses;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(UUID clientId, PlaceOrderRequest request) {
        //persist order with NEW status first
        Order order = Order.builder()
                .orderStatus(OrderStatus.NEW)
                .creationTime(ZonedDateTime.now())
                .lastUpdateTime(ZonedDateTime.now())
                .clientId(clientId)
                //.clientName() TODO: fetch client info from user service
                .shippingAddress(request.getAddress())
                .build();
        List<OrderProduct> orderProducts = new ArrayList<>();
        for(ProductOfOrderRequest product : request.getProducts()){
            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(UUID.fromString(product.getId()))
                    .purchasedQuantity(product.getQuantity())
                    .fulfilledQuantity(0)
                    .build();
            orderProducts.add(orderProduct);
        }
        order.setProducts(orderProducts);
        order = orderRepository.save(order);
        OrderResponse response = OrderResponse
                .builder()
                .orderId(order.getOrderId().toString())
                .status(order.getOrderStatus().getValue())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreationTime())
                .updatedAt(order.getLastUpdateTime())
                .build();
        List<ProductOfOrderResponse> productOfOrderResponses = new ArrayList<>();
        for(OrderProduct orderProduct : order.getProducts()){
            ProductOfOrderResponse product = ProductOfOrderResponse.builder()
                    .id(orderProduct.getProductId().toString())
                    .name(orderProduct.getProductName())
                    .imageUrl(orderProduct.getImageUrl())
                    .purchasedQuantity(orderProduct.getPurchasedQuantity())
                    .fulfilledQuantity(orderProduct.getFulfilledQuantity())
                    .unitPrice(orderProduct.getUnitPrice())
                    .totalPrice(order.getTotalPrice())
                    .build();
        }
        response.setProducts(productOfOrderResponses);

        //call product service to validate products
        orderProcessingAsync.validate(order.getOrderId(), request);
        return response;
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException(orderId.toString()));
        OrderStatus newStatus = OrderStatus.getFromValue(status);
        if(order.getOrderStatus().canUpdateTo(order.getOrderStatus(), newStatus)){
            order.setOrderStatus(newStatus);
            order.setLastUpdateTime(ZonedDateTime.now());
            orderRepository.save(order);
            return OrderResponse.builder()
                    .orderId(order.getOrderId().toString())
                    .status(order.getOrderStatus().getValue())
                    .totalPrice(order.getTotalPrice())
                    .createdAt(order.getCreationTime())
                    .updatedAt(order.getLastUpdateTime())
                    .build();
        }else{
            throw new OrderStatusUpdateException(order.getOrderStatus().getValue(), status);
        }
    }

    @Override
    public OrderResponse getOrderById(UUID orderId, boolean isAdmin, UUID userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException(orderId.toString()));
        if(!isAdmin){
            if(!order.getClientId().equals(userId)){
                throw new AuthorizationException("only admin can access other user's order");
            }
        }
        return OrderResponse.builder()
                .orderId(order.getOrderId().toString())
                .status(order.getOrderStatus().getValue())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreationTime())
                .updatedAt(order.getLastUpdateTime())
                .build();
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID orderId, boolean isAdmin, UUID userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException(orderId.toString()));
        if(!isAdmin){
            if(!order.getClientId().equals(userId)){
                throw new AuthorizationException("you are not authorized to cancel this order");
            }
        }
        return updateOrderStatus(orderId, OrderStatus.CANCELLED.getValue());
    }

    @Transactional
    public void handleValidationResponse(UUID orderId, ProductValidationResponse response){
        Order order = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException(orderId.toString()));
        List<OrderProduct> orderProducts = order.getProducts();

        //check the response is valid (1-1 match of response and order)
        Map<UUID, OrderProduct> orderProductMap =
                orderProducts.stream()
                        .collect(Collectors.toMap(
                                OrderProduct::getProductId,
                                Function.identity()
                        ));
        List<IndividualProductValidationResponse> responseProducts =
                response.getProducts();

        if (responseProducts.size() != orderProductMap.size()) {
            throw new ValidationException("Product count mismatch");
        }

        for (IndividualProductValidationResponse resp : responseProducts) {
            OrderProduct orderProduct = orderProductMap.get(UUID.fromString(resp.getProductId()));
            if (orderProduct == null) {
                throw new ValidationException(
                        "Unexpected product in validation response: " + resp.getProductId()
                );
            }
            if (!orderProduct.getPurchasedQuantity().equals(resp.getRequestedQuantity())) {
                throw new ValidationException(
                        "Quantity mismatch for product " + resp.getProductId()
                );
            }
        }

        //update order status
        if(response.getResult()== ValidationResult.FULL){
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }
        if (response.getResult()==ValidationResult.PARTIAL) {
            order.setOrderStatus(OrderStatus.PARTIAL_CONFIRMED);
        }
        if (response.getResult()==ValidationResult.NONE){
            order.setOrderStatus(OrderStatus.CANCELLED);
            return;
        }
        order.setLastUpdateTime(ZonedDateTime.now());

        //update order
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(IndividualProductValidationResponse product : responseProducts){
            BigDecimal price = product.getUnitPrice().multiply(BigDecimal.valueOf(product.getDeductedQuantity()));
            totalPrice = totalPrice.add(price);
            OrderProduct orderProduct = orderProductMap.get(UUID.fromString(product.getProductId()));
            orderProduct.setFulfilledQuantity(product.getDeductedQuantity());
            orderProduct.setUnitPrice(product.getUnitPrice());
            orderProduct.setTotalPrice(price);
            orderProduct.setImageUrl(product.getProductImageUrl());
            orderProduct.setProductName(product.getProductName());
        }
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
    }

}
