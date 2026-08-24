package com.pensieve.application;

import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    void save(Review review);

    Optional<Review> findById(UUID id);

    Optional<Review> findByIdAndUserId(UUID id, UUID userId);

    List<PendingReviewDto> findPendingReviews(UUID userId);
}
