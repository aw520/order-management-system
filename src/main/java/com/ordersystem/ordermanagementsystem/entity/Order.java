package com.ordersystem.ordermanagementsystem.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

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
    private String orderId;
    private Integer orderStatus;
    private String orderQuantity;
    private String price;
    private String currency;
    private ZonedDateTime creationTime;
    private ZonedDateTime lastUpdateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //TODO: relationship between order and products
}