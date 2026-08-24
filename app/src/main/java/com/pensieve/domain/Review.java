package com.pensieve.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
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
        this.status = ReviewStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void evaluate(EvaluateResult result) {
        Objects.requireNonNull(result, "Evaluation result is required");

        if (status != ReviewStatus.PENDING) {
            throw new IllegalStateException("Only pending reviews can be evaluated");
        }

        switch (result) {
            case REMEMBERED -> advanceToNextInterval();
            case FORGOTTEN -> rescheduleForFirstInterval();
        }
    }

    private void advanceToNextInterval() {
        var nextInterval = switch (intervalDays) {
            case 1 -> 7;
            case 7 -> 30;
            case 30 -> 180;
            case 180 -> 0;
            default -> throw new IllegalStateException("Unsupported review interval: " + intervalDays);
        };

        completedAt = LocalDateTime.now();

        if (nextInterval == 0) {
            status = ReviewStatus.COMPLETED;
            return;
        }

        intervalDays = nextInterval;
        scheduledFor = LocalDate.now().plusDays(nextInterval);
    }

    private void rescheduleForFirstInterval() {
        intervalDays = 1;
        scheduledFor = LocalDate.now().plusDays(1);
        completedAt = LocalDateTime.now();
        status = ReviewStatus.PENDING;
    }
}
