package com.pensieve.application;

import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.Review;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    void save(Review review);

    void saveAll(List<Review> reviews);

    Optional<Review> findById(UUID id);

    List<PendingReviewDto> findPendingReviews(LocalDate date);
}
