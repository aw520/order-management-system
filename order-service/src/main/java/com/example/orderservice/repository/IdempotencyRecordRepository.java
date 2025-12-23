package com.example.orderservice.repository;

import com.example.orderservice.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, UUID> {

}
