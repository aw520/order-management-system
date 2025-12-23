package com.example.productservice.repository;

import com.example.productservice.entity.IdempotencyRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IdempotencyRecordRepositoryTest {

    @Autowired
    private IdempotencyRecordRepository idempotencyRecordRepository;

    @Test
    void findByIdempotencyKey_shouldReturnRecord() {
        UUID key = UUID.randomUUID();

        IdempotencyRecord record = IdempotencyRecord.builder()
                .idempotencyKey(key)
                .createdAt(LocalDateTime.now())
                .build();

        idempotencyRecordRepository.save(record);

        IdempotencyRecord found =
                idempotencyRecordRepository.findByIdempotencyKey(key);

        assertThat(found).isNotNull();
        assertThat(found.getIdempotencyKey()).isEqualTo(key);
    }

    @Test
    void deleteByCreatedAtBefore_shouldDeleteOldRecords() {
        IdempotencyRecord oldRecord = IdempotencyRecord.builder()
                .idempotencyKey(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        IdempotencyRecord newRecord = IdempotencyRecord.builder()
                .idempotencyKey(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        idempotencyRecordRepository.save(oldRecord);
        idempotencyRecordRepository.save(newRecord);

        int deleted = idempotencyRecordRepository
                .deleteByCreatedAtBefore(LocalDateTime.now().minusDays(1));

        assertThat(deleted).isEqualTo(1);
        assertThat(idempotencyRecordRepository.findAll())
                .hasSize(1);
    }
}

