package com.ordersystem.ordermanagementsystem.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productDescription;
    private String imageUrl;
    private String currency;
    private Integer quantity;
    //TODO: Relationship between products and orders

}
