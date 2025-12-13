package com.ordersystem.ordermanagementsystem.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private String imageUrl;
    private String currency;
    private Integer quantity;
    @Version
    private Long version;
    //Relationship between products and orders

}
