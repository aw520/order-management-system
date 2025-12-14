package com.ordersystem.ordermanagementsystem.service.impl;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.dto.RequestOrderItem;
import com.ordersystem.ordermanagementsystem.entity.*;
import com.ordersystem.ordermanagementsystem.exception.*;
import com.ordersystem.ordermanagementsystem.repository.*;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.response.OrderResponse;
import com.ordersystem.ordermanagementsystem.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderServiceImpl.class)
class OrderServiceImplTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderRepositoryCustom orderRepositoryCustom;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "CLIENT")
    void createOrderWithoutUserId_asClient_success() {
        // given
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
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
        OrderResponse response = orderService.createOrder(request);

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
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "CLIENT")
    void createOrderWithSelfUserId_asClient_success() {
        // given
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
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
                .userId(userId.toString())
                .orderItems(List.of(item))
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        OrderResponse response = orderService.createOrder(request);

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
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = {"CLIENT", "ADMIN"})
    void createOrder_asAdmin_success() {
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
                .userId(userId.toString())
                .orderItems(List.of(item))
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        OrderResponse response = orderService.createOrder(request);

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
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "CLIENT")
    void createOrderForOther_asClient_throws() {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .userId(UUID.randomUUID().toString()) // other user
                .currency("USD")
                .orderItems(List.of(
                        RequestOrderItem.builder()
                                .productId(UUID.randomUUID())
                                .quantity(1)
                                .build()
                ))
                .build();

        // when
        assertThrows(PermissionDeniedException.class,
                () -> orderService.createOrder(request)
        );

        // then
        verifyNoInteractions(userRepository);
        verifyNoInteractions(productRepository);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = "CLIENT"
    )
    void createOrder_userNotFound_throws() {
        // given
        UUID userId =
                UUID.fromString("11111111-1111-1111-1111-111111111111");

        OrderCreateRequest request = OrderCreateRequest.builder()
                .currency("USD")
                .orderItems(List.of()) // fails at user lookup first
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(UserNotFoundException.class,
                () -> orderService.createOrder(request)
        );

        verify(userRepository).findById(userId);
        verifyNoInteractions(productRepository);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = "CLIENT"
    )
    void createOrder_productNotFound_throws() {
        // given
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        //UUID userId = UUID.randomUUID();
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
                orderService.createOrder(request)
        );

        verify(userRepository).findById(userId);
        verify(productRepository).findById(productId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = "ADMIN"
    )
    void confirmOrder_asAdmin_success_stockDeducted() {
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
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = "CLIENT"
    )
    void confirmOrder_asClient_throws() {
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
        assertThrows(PermissionDeniedException.class, () ->
                        orderService.confirmOrder(orderId)
        );

        // stock unchanged
        assertEquals(10, product.getQuantity());
        assertEquals(OrderStatus.NEW, order.getOrderStatus());

        //verify(orderRepository).findById(orderId);
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = "ADMIN"
    )
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
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = "ADMIN"
    )
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
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"CLIENT","ADMIN"}
    )
    void updateOrderStatus_asAdmin_confirmedToShipped_success() {
        // given
        UUID orderId = UUID.randomUUID();

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        OrderResponse response =
                orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);

        // then
        assertEquals(OrderStatus.SHIPPED, response.getStatus());
        assertNotNull(response.getUpdatedAt());

        verify(orderRepository).findById(orderId);
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"CLIENT"}
    )
    void updateOrderStatus_asClient_throws() {
        // given
        UUID orderId = UUID.randomUUID();

        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        assertThrows(PermissionDeniedException.class, () ->
                orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED)
        );

        // then
        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());
        assertNull(order.getLastUpdateTime());
        verify(orderRepository).findById(orderId);
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
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
                orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED)
        );

        // ensure nothing changed
        assertEquals(OrderStatus.NEW, order.getOrderStatus());
        assertNull(order.getLastUpdateTime());

        verify(orderRepository).findById(orderId);
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
    void cancelOrder_asAdmin_success_restoresStock() {
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

        User user = User.builder().userId(userId).build();
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .user(user)
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        OrderResponse response = orderService.cancelOrder(orderId);

        // then
        assertEquals(OrderStatus.CANCELLED, response.getStatus());
        assertEquals(8, product.getQuantity()); // 5 + 3 restored
        assertNotNull(response.getUpdatedAt());

        verify(orderRepository).findById(orderId);
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"CLIENT"}
    )
    void cancelOrder_asClient_success_restoresStock() {
        // given
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("Apple")
                .quantity(5) // stock after confirmation
                .build();

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(3);

        User user = User.builder().userId(userId).build();
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .user(user)
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        OrderResponse response = orderService.cancelOrder(orderId);

        // then
        assertEquals(OrderStatus.CANCELLED, response.getStatus());
        assertEquals(8, product.getQuantity()); // 5 + 3 restored
        assertNotNull(response.getUpdatedAt());

        verify(orderRepository).findById(orderId);
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"CLIENT"}
    )
    void cancelOrderForOther_asClient_throws() {
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

        User user = User.builder().userId(userId).build();
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CONFIRMED)
                .orderProducts(new ArrayList<>(List.of(orderProduct)))
                .user(user)
                .build();

        orderProduct.setOrder(order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        assertThrows(PermissionDeniedException.class, () ->orderService.cancelOrder(orderId));

        // then
        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());
        assertEquals(5, product.getQuantity());
        assertNull(order.getLastUpdateTime());

        verify(orderRepository).findById(orderId);
    }

    @Test
    @WithMockUser(
            username = "11111111-1111-1111-1111-111111111111",
            roles = {"ADMIN"}
    )
    void cancelOrder_invalidStatus_throws() {
        // given
        UUID orderId = UUID.randomUUID();

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
                orderService.cancelOrder(orderId)
        );

        // ensure nothing changed
        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
        assertEquals(5, product.getQuantity());
        assertNull(order.getLastUpdateTime());

        verify(orderRepository).findById(orderId);
    }

    //TODO: SearchTest

}
