package com.pensieve.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Review {

    private UUID id;
    private UUID triggerId;
    private int intervalDays;
    private LocalDate scheduledFor;
    private LocalDateTime completedAt;
    private ReviewStatus status;
    private LocalDateTime createdAt;

    public Review(UUID triggerId, int intervalDays, LocalDate scheduledFor) {
        this.id = UUID.randomUUID();
        this.triggerId = triggerId;
        this.intervalDays = intervalDays;
        this.scheduledFor = scheduledFor;
        this.status = ReviewStatus.PENDING_1;
        this.createdAt = LocalDateTime.now();
    }

    public void evaluate(EvaluateResult result) {
        this.completedAt = LocalDateTime.now();
        switch (result) {
            case REMEMBERED -> this.status = ReviewStatus.COMPLETED;
            case FORGOTTEN -> this.status = ReviewStatus.FAILED;
        }
    }
}
