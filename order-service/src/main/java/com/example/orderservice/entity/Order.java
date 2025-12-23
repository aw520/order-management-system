package com.example.orderservice.entity;

import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.converter.OrderStatusConverter;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_orders_client_id", columnList = "client_id"),
                @Index(name = "idx_orders_status", columnList = "order_status"),
                @Index(name = "idx_orders_creation_time", columnList = "creation_time")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @Convert(converter = OrderStatusConverter.class)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    private BigDecimal price;

    @NotNull
    private ZonedDateTime creationTime;

    @NotNull
    private ZonedDateTime lastUpdateTime;

    @NotNull
    private UUID clientId;

    private String clientName;
    private String shippingAddress;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderProduct> products = new ArrayList<>();
}
