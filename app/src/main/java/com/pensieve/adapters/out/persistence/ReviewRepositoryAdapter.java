package com.pensieve.adapters.out.persistence;

import com.pensieve.adapters.out.persistence.jpa.ReviewJpaRepository;
import com.pensieve.adapters.mappers.ReviewMapper;
import com.pensieve.application.ReviewRepository;
import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.Review;
import com.pensieve.domain.ReviewStatus;
import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;
    private final ReviewMapper mapper;

    public ReviewRepositoryAdapter(ReviewJpaRepository jpaRepository, ReviewMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Review review) {
        jpaRepository.save(mapper.toEntity(review));
    }

    @Override
    public void saveAll(List<Review> reviews) {
        jpaRepository.saveAll(reviews.stream().map(mapper::toEntity).collect(Collectors.toList()));
    }

    @Override
    public Optional<Review> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<PendingReviewDto> findPendingReviews(UUID userId, LocalDate date) {
        return jpaRepository.findPendingReviewsWithDetails(date, ReviewStatus.PENDING, userId);
    }
}
