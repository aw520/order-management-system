package com.example.productservice.entity;

import jakarta.persistence.*;
import com.example.common.kafka.ProductValidationResponse;
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
    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID idempotencyKey;

    @JdbcTypeCode(SqlTypes.JSON)
    private ProductValidationResponse productValidationResponse;

    private LocalDateTime createdAt;


}
