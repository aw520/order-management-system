package com.example.productservice.repository;

import com.example.productservice.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, String> {
    IdempotencyRecord findByIdempotencyKey(UUID requestId);
    int deleteByCreatedAtBefore(LocalDateTime dateTime);

}
