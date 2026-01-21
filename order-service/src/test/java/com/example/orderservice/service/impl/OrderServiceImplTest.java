package com.example.orderservice.service.impl;

import com.example.common.kafka.IndividualProductValidationResponse;
import com.example.orderservice.constant.OrderStatus;
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
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.service.kafka.ProductValidationProducer;
import com.example.common.kafka.ProductValidationResponse;
import com.example.common.kafka.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;

    @Mock
    private ProductValidationProducer productValidationProducer;


    @InjectMocks
    private OrderServiceImpl orderService;

    private Order buildOrder(UUID orderId, UUID clientId) {
        return Order.builder()
                .orderId(orderId)
                .clientId(clientId)
                .orderStatus(OrderStatus.NEW)
                .totalPrice(BigDecimal.valueOf(100))
                .creationTime(ZonedDateTime.now().minusDays(1))
                .lastUpdateTime(ZonedDateTime.now())
                .build();
    }

    @Test
    void getOrderById_adminAccess_success() {
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        Order order = buildOrder(orderId, clientId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponse response =
                orderService.getOrderById(orderId, true, UUID.randomUUID());


        assertEquals(orderId.toString(), response.getOrderId());
        assertEquals(OrderStatus.NEW.getValue(), response.getStatus());
        assertEquals(order.getTotalPrice(), response.getTotalPrice());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrderById_nonAdminOwnOrder_success() {
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        Order order = buildOrder(orderId, clientId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponse response =
                orderService.getOrderById(orderId, false, clientId);

        assertEquals(orderId.toString(), response.getOrderId());
        assertEquals(OrderStatus.NEW.getValue(), response.getStatus());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrderById_nonAdminOtherUsersOrder_throwsAuthorizationException() {
        UUID orderId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        Order order = buildOrder(orderId, ownerId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(
                AuthorizationException.class,
                () -> orderService.getOrderById(orderId, false, otherUserId)
        );

        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrderById_orderNotFound_throwsException() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(orderId, true, UUID.randomUUID())
        );

        verify(orderRepository).findById(orderId);
    }


    private Order buildOrder(UUID orderId, UUID clientId, OrderStatus status) {
        return Order.builder()
                .orderId(orderId)
                .clientId(clientId)
                .orderStatus(status)
                .totalPrice(BigDecimal.valueOf(100))
                .creationTime(ZonedDateTime.now().minusDays(1))
                .lastUpdateTime(ZonedDateTime.now())
                .build();
    }

    @Test
    void updateOrderStatus_validTransition_success() {
        UUID orderId = UUID.randomUUID();

        Order order = buildOrder(orderId, UUID.randomUUID(), OrderStatus.NEW);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse response =
                orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED.getValue());

        assertEquals(OrderStatus.CONFIRMED.getValue(), response.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatus_invalidTransition_throwsException() {
        UUID orderId = UUID.randomUUID();

        Order order = buildOrder(orderId, UUID.randomUUID(), OrderStatus.SHIPPED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(
                OrderStatusUpdateException.class,
                () -> orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED.getValue())
        );

        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrderStatus_orderNotFound_throwsException() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED.getValue())
        );
    }

    private OrderProduct buildOrderProduct(UUID productId, int purchasedQty) {
        return OrderProduct.builder()
                .productId(productId)
                .purchasedQuantity(purchasedQty)
                .fulfilledQuantity(0)
                .build();
    }

    private Order buildOrderWithProducts(UUID orderId, UUID clientId, List<OrderProduct> products) {
        Order order = Order.builder()
                .orderId(orderId)
                .clientId(clientId)
                .orderStatus(OrderStatus.NEW)
                .creationTime(ZonedDateTime.now().minusDays(1))
                .lastUpdateTime(ZonedDateTime.now())
                .build();
        order.setProducts(products);
        return order;
    }

    private IndividualProductValidationResponse buildValidationProduct(
            UUID productId,
            int requestedQty,
            int deductedQty,
            BigDecimal unitPrice
    ) {
        return IndividualProductValidationResponse.builder()
                .productId(productId.toString())
                .requestedQuantity(requestedQty)
                .deductedQuantity(deductedQty)
                .unitPrice(unitPrice)
                .productName("test-product")
                .productImageUrl("img")
                .build();
    }

    @Test
    void handleValidationResponse_fullConfirmation_success() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderProduct orderProduct = buildOrderProduct(productId, 2);
        Order order = buildOrderWithProducts(orderId, UUID.randomUUID(), List.of(orderProduct));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ProductValidationResponse response = ProductValidationResponse.builder()
                .result(ValidationResult.FULL)
                .products(List.of(
                        buildValidationProduct(productId, 2, 2, BigDecimal.valueOf(10))
                ))
                .build();

        orderService.handleValidationResponse(orderId, response);

        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());
        assertEquals(BigDecimal.valueOf(20), order.getTotalPrice());
        assertEquals(2, orderProduct.getFulfilledQuantity());
        assertEquals(BigDecimal.valueOf(10), orderProduct.getUnitPrice());

        verify(orderRepository).save(order);
    }

    @Test
    void handleValidationResponse_partialConfirmation_success() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderProduct orderProduct = buildOrderProduct(productId, 5);
        Order order = buildOrderWithProducts(orderId, UUID.randomUUID(), List.of(orderProduct));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ProductValidationResponse response = ProductValidationResponse.builder()
                .result(ValidationResult.PARTIAL)
                .products(List.of(
                        buildValidationProduct(productId, 5, 3, BigDecimal.valueOf(10))
                ))
                .build();

        orderService.handleValidationResponse(orderId, response);

        assertEquals(OrderStatus.PARTIAL_CONFIRMED, order.getOrderStatus());
        assertEquals(BigDecimal.valueOf(30), order.getTotalPrice());
        assertEquals(3, orderProduct.getFulfilledQuantity());

        verify(orderRepository).save(order);
    }

    @Test
    void handleValidationResponse_noneConfirmation_cancelsOrder() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderProduct orderProduct = buildOrderProduct(productId, 2);
        Order order = buildOrderWithProducts(orderId, UUID.randomUUID(), List.of(orderProduct));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ProductValidationResponse response = ProductValidationResponse.builder()
                .result(ValidationResult.NONE)
                .products(List.of(
                        buildValidationProduct(productId, 2, 0, BigDecimal.valueOf(10))
                ))
                .build();

        orderService.handleValidationResponse(orderId, response);

        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void handleValidationResponse_productCountMismatch_throwsException() {
        UUID orderId = UUID.randomUUID();

        Order order = buildOrderWithProducts(
                orderId,
                UUID.randomUUID(),
                List.of(buildOrderProduct(UUID.randomUUID(), 1))
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ProductValidationResponse response = ProductValidationResponse.builder()
                .result(ValidationResult.FULL)
                .products(List.of()) // mismatch
                .build();

        assertThrows(
                ValidationException.class,
                () -> orderService.handleValidationResponse(orderId, response)
        );

        verify(orderRepository, never()).save(any());
    }


    @Test
    void handleValidationResponse_quantityMismatch_throwsException() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderProduct orderProduct = buildOrderProduct(productId, 5);
        Order order = buildOrderWithProducts(orderId, UUID.randomUUID(), List.of(orderProduct));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ProductValidationResponse response = ProductValidationResponse.builder()
                .result(ValidationResult.FULL)
                .products(List.of(
                        buildValidationProduct(productId, 4, 4, BigDecimal.valueOf(10)) // mismatch
                ))
                .build();

        assertThrows(
                ValidationException.class,
                () -> orderService.handleValidationResponse(orderId, response)
        );

        verify(orderRepository, never()).save(any());
    }


    @Test
    void placeOrder_sendsKafkaMessage_andReturnsResponse() {
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .address("test-address")
                .products(List.of(
                        new ProductOfOrderRequest(productId.toString(), 2)
                ))
                .build();
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order o = invocation.getArgument(0);
                    if (o.getOrderId() == null) {
                        o.setOrderId(UUID.randomUUID());
                    }
                    return o;
                });

        //when
        OrderResponse response = orderService.placeOrder(clientId, request);

        //then: response is returned immediately
        assertEquals(OrderStatus.NEW.getValue(), response.getStatus());
        assertEquals(1, response.getProducts().size());

        //Kafka producer invoked exactly once
        verify(productValidationProducer, times(1))
                .send(any(UUID.class), eq(request));

        // ensure no other async behavior
        verifyNoMoreInteractions(productValidationProducer);
    }



}

