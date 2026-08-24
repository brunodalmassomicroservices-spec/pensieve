package com.pensieve.application.records;

import java.util.UUID;

public record PendingReviewDto(
        UUID reviewId,
        UUID trigger,
        String subject,
        String triggerTitle,
        String notes,
        int intervalDays
) {}
