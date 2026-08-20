package com.pensieve.adapters.mappers;

import com.pensieve.domain.ReviewStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ReviewEvaluateResult(UUID reviewId, ReviewStatus status, LocalDate nextReviewDate) {
}
