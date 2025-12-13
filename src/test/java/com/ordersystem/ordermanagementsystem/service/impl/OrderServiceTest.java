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


}
