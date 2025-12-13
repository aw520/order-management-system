package com.ordersystem.ordermanagementsystem.service.impl;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.RequestOrderItem;
import com.ordersystem.ordermanagementsystem.entity.*;
import com.ordersystem.ordermanagementsystem.exception.*;
import com.ordersystem.ordermanagementsystem.repository.*;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_success() {
        // given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        User user = User.builder()
                .userId(userId)
                .build();

        Product product = Product.builder()
                .productId(productId)
                .productName("Apple")
                .productPrice(new BigDecimal("10.00"))
                .quantity(100)
                .build();

        RequestOrderItem item = RequestOrderItem.builder()
                .productId(productId)
                .quantity(2)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .currency("USD")
                .orderItems(List.of(item))
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        OrderResponse response = orderService.createOrder(request, userId);

        // then
        assertNotNull(response);
        assertEquals(OrderStatus.NEW, response.getStatus());
        assertEquals(new BigDecimal("20.00"), response.getTotalPrice());
        assertEquals("USD", response.getCurrency());
        assertEquals(1, response.getItems().size());

        assertEquals("Apple", response.getItems().get(0).getProductName());
        assertEquals(2, response.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("10.00"), response.getItems().get(0).getUnitPrice());

        verify(userRepository).findById(userId);
        verify(productRepository).findById(productId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_userNotFound_throws() {
        // given
        UUID userId = UUID.randomUUID();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .currency("USD")
                .orderItems(List.of()) // empty is fine, we fail earlier
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(UserNotFoundException.class, () ->
                orderService.createOrder(request, userId)
        );

        verify(userRepository).findById(userId);
        verifyNoInteractions(productRepository);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_productNotFound_throws() {
        // given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        User user = User.builder()
                .userId(userId)
                .build();

        RequestOrderItem item = RequestOrderItem.builder()
                .productId(productId)
                .quantity(1)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .currency("USD")
                .orderItems(List.of(item))
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(ProductNotFoundException.class, () ->
                orderService.createOrder(request, userId)
        );

        verify(userRepository).findById(userId);
        verify(productRepository).findById(productId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void confirmOrder_success_stockDeducted() {
        // given
        UUID orderId = UUID.randomUUID();

        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("Apple")
                .quantity(10)
                .build();

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(3);

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.NEW)
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        OrderResponse response = orderService.confirmOrder(orderId);

        // then
        assertEquals(OrderStatus.CONFIRMED, response.getStatus());
        assertEquals(7, product.getQuantity());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void confirmOrder_outOfStock_throws() {
        // given
        UUID orderId = UUID.randomUUID();

        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("Apple")
                .quantity(2) // insufficient stock
                .build();

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(5); // order wants more than available

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.NEW)
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when + then
        assertThrows(OrderConfirmationFailedException.class, () ->
                orderService.confirmOrder(orderId)
        );

        // stock unchanged
        assertEquals(2, product.getQuantity());
        assertEquals(OrderStatus.NEW, order.getOrderStatus());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void confirmOrder_invalidStatus_throws() {
        // given
        UUID orderId = UUID.randomUUID();

        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("Apple")
                .quantity(10)
                .build();

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(2);

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED) // already confirmed
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when + then
        assertThrows(OrderConfirmationFailedException.class, () ->
                orderService.confirmOrder(orderId)
        );

        // ensure nothing changed
        assertEquals(10, product.getQuantity());
        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void updateOrderStatus_confirmedToShipped_success() {
        // given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        OrderResponse response =
                orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED, userId);

        // then
        assertEquals(OrderStatus.SHIPPED, response.getStatus());
        assertNotNull(response.getUpdatedAt());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void updateOrderStatus_invalidTransition_throws() {
        // given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.NEW) // invalid start state
                .build();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when + then
        assertThrows(OrderStatusUpdateFailedException.class, () ->
                orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED, userId)
        );

        // ensure nothing changed
        assertEquals(OrderStatus.NEW, order.getOrderStatus());
        assertNull(order.getLastUpdateTime());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void cancelOrder_success_restoresStock() {
        // given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("Apple")
                .quantity(5) // stock after confirmation
                .build();

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(3);

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        OrderResponse response = orderService.cancelOrder(orderId, userId);

        // then
        assertEquals(OrderStatus.CANCELLED, response.getStatus());
        assertEquals(8, product.getQuantity()); // 5 + 3 restored
        assertNotNull(response.getUpdatedAt());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void cancelOrder_invalidStatus_throws() {
        // given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("Apple")
                .quantity(5)
                .build();

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(3);

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.SHIPPED) // cannot cancel
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when + then
        assertThrows(CancellationFailedException.class, () ->
                orderService.cancelOrder(orderId, userId)
        );

        // ensure nothing changed
        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
        assertEquals(5, product.getQuantity());
        assertNull(order.getLastUpdateTime());

        verify(orderRepository).findById(orderId);
    }

}
