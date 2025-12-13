package com.ordersystem.ordermanagementsystem.entity;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private BigDecimal price;
    private String currency;
    private ZonedDateTime creationTime;
    private ZonedDateTime lastUpdateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //TODO: relationship between order and products
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderProduct> orderProducts;

    public void addProduct(OrderProduct product) {
        orderProducts.add(product);
        product.setOrder(this);
    }

    public void removeProduct(OrderProduct product) {
        orderProducts.remove(product);
        product.setOrder(null);
    }
}