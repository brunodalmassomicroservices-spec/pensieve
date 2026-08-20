package com.pensieve.adapters.out.persistence.entity;

import com.pensieve.domain.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "reviews",
        indexes = {
                @Index(name = "idx_reviews_scheduled_status", columnList = "scheduled_for, status")
        })
@Getter
@Setter
public class ReviewEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trigger_id")
    private TriggerEntity trigger;

    @Column(name = "interval_days")
    private int intervalDays;

    @Column(name = "scheduled_for")
    private LocalDate scheduledFor;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
