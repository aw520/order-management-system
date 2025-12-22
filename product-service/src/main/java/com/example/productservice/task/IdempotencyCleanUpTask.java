package com.example.productservice.task;

import com.example.productservice.repository.IdempotencyRecordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
public class IdempotencyCleanUpTask{

    private final IdempotencyRecordRepository repository;

    public IdempotencyCleanUpTask(IdempotencyRecordRepository repository) {
        this.repository = repository;
    }

    // Runs at 3 AM every night to avoid peak traffic
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void clearOldIdempotencyKeys() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        int deletedCount = repository.deleteByCreatedAtBefore(cutoff);
        //TODO: log the result
        //System.out.println("Cleaned up " + deletedCount + " idempotency records.");
    }
}
