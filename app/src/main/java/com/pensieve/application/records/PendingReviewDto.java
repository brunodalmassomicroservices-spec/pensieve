package com.pensieve.application.records;

import java.util.UUID;

public record PendingReviewDto(
        UUID reviewId,
        UUID triggerId,
        String subjectName,
        String triggerTitle,
        String notes,
        int intervalDays
) {}
