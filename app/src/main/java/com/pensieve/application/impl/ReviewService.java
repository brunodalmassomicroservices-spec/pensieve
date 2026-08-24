package com.pensieve.application.impl;

import com.pensieve.adapters.mappers.ReviewEvaluateResult;
import com.pensieve.application.ReviewRepository;
import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.EvaluateResult;
import com.pensieve.domain.ReviewStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        review.evaluate(result);

        reviewRepository.save(review);

        LocalDate nextReviewDate = review.getStatus() == ReviewStatus.PENDING
                ? review.getScheduledFor()
                : null;

        return new ReviewEvaluateResult(review.getId(), review.getStatus(), nextReviewDate);
    }

    @Transactional(readOnly = true)
    public List<PendingReviewDto> getTodaysReviews(UUID userId) {
        return reviewRepository.findPendingReviews(userId);
    }
}
