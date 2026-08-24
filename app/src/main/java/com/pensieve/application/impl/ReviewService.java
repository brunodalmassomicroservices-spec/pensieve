package com.pensieve.application.impl;

import com.pensieve.adapters.mappers.ReviewEvaluateResult;
import com.pensieve.application.ReviewRepository;
import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.EvaluateResult;
import com.pensieve.domain.Review;
import com.pensieve.domain.ReviewStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public ReviewEvaluateResult evaluateReview(UUID reviewId, EvaluateResult result) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        review.evaluate(result); // Aplica RN-02 ou RN-03[cite: 3]
        reviewRepository.save(review);

        LocalDate nextReviewDate = null;

        // RN-03: Agendamento emergencial para D+1 em caso de falha[cite: 3]
        if (review.getStatus() == ReviewStatus.FAILED) {
            nextReviewDate = LocalDate.now().plusDays(1);
            var emergencyReview = new Review(review.getTriggerId(), 1, nextReviewDate);
            reviewRepository.save(emergencyReview);
        }

        return new ReviewEvaluateResult(review.getId(), review.getStatus(), nextReviewDate);
    }

    @Transactional(readOnly = true)
    public List<PendingReviewDto> getTodaysReviews(UUID userId) {
        // RN-04: status = 'PENDING' e scheduled_for <= CURRENT_DATE[cite: 3]
        return reviewRepository.findPendingReviews(userId, LocalDate.now());
    }
}
