package com.pensieve.adapters.mappers;

import java.util.UUID;

public record TriggerCreateResult(
        UUID id,
        UUID userId,
        String subject,
        String title,
        int createdReviewsCount) {
}