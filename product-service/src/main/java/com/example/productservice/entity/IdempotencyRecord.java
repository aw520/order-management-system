package com.example.productservice.entity;

import com.example.productservice.response.ProductValidationResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "idempotency_records")
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idempotencyKey;

    @JdbcTypeCode(SqlTypes.JSON)
    private ProductValidationResponse productValidationResponse;

    private LocalDateTime createdAt;


}
