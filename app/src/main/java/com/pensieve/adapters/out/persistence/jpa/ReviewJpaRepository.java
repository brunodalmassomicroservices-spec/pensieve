package com.pensieve.adapters.out.persistence.jpa;

import com.pensieve.adapters.out.persistence.entity.ReviewEntity;
import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, UUID> {
    @Query("""
                SELECT new com.pensieve.application.records.PendingReviewDto(
                    r.id, t.id, t.title, t.title, t.notes, r.intervalDays
                ) 
                FROM ReviewEntity r 
                JOIN r.trigger t
               WHERE r.status = :status 
                 AND r.scheduledFor <= :date
                 AND t.users.id = :user
            """)
    List<PendingReviewDto> findPendingReviewsWithDetails(@Param("date") LocalDate date, @Param("status") ReviewStatus status, @Param("user") UUID user);
}
