package com.pensieve.application.impl;

import com.pensieve.adapters.mappers.TriggerCreateResult;
import com.pensieve.application.ReviewRepository;
import com.pensieve.application.TriggerRepository;
import com.pensieve.domain.Review;
import com.pensieve.domain.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TriggerService {

    private final TriggerRepository triggerRepository;
    private final ReviewRepository reviewRepository;

    public TriggerService(TriggerRepository triggerRepository, ReviewRepository reviewRepository) {
        this.triggerRepository = triggerRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public TriggerCreateResult createTrigger(UUID clientId, String subject, String title, String notes) {

        var trigger = new Trigger(clientId, subject, title, notes);
        triggerRepository.save(trigger);

        // RN-01 e ADR-001: Agendamento fixo para 1, 7, 30 e 180 dias[cite: 1, 3]
        var intervals = List.of(1, 7, 30, 180);
        var today = LocalDate.now();

        var reviews = intervals.stream()
                .map(interval -> new Review(trigger.getId(), interval, today.plusDays(interval)))
                .toList();

        reviewRepository.saveAll(reviews);

        return new TriggerCreateResult(trigger.getId(), trigger.getClientId(), trigger.getSubject(), trigger.getTitle(), reviews.size());
    }
}