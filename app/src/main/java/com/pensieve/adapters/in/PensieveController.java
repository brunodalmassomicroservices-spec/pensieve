package com.pensieve.adapters.in;


import com.pensieve.adapters.mappers.ReviewEvaluateRequest;
import com.pensieve.adapters.mappers.ReviewEvaluateResponse;
import com.pensieve.adapters.mappers.TriggerCreateRequest;
import com.pensieve.adapters.mappers.TriggerCreateResponse;
import com.pensieve.application.ReviewService;
import com.pensieve.application.TriggerService;
import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.EvaluateResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PensieveController {

    private final TriggerService triggerService;
    private final ReviewService reviewService;

    public PensieveController(TriggerService triggerService, ReviewService reviewService) {
        this.triggerService = triggerService;
        this.reviewService = reviewService;
    }

    @PostMapping("/triggers")
    @ResponseStatus(HttpStatus.CREATED)
    public TriggerCreateResponse createTrigger(@RequestBody TriggerCreateRequest request) {
        var result = triggerService.createTrigger(
                request.subject_id(),
                request.title(),
                request.notes()
        );
        return new TriggerCreateResponse(result.id(), result.title(), result.createdReviewsCount());
    }

    public record ReviewsTodayResponse(int total_pending, List<PendingReviewDto> items) {}

    @GetMapping("/reviews/today")
    public ReviewsTodayResponse getTodaysReviews() {
        var items = reviewService.getTodaysReviews();
        return new ReviewsTodayResponse(items.size(), items); // Retorna a lista e o total[cite: 2]
    }

    @PostMapping("/reviews/{id}/evaluate")
    public ReviewEvaluateResponse evaluateReview(
            @PathVariable UUID id,
            @RequestBody ReviewEvaluateRequest request) {

        var evaluateResult = EvaluateResult.valueOf(request.result().toUpperCase()); // REMEMBERED ou FORGOTTEN[cite: 2]
        var result = reviewService.evaluateReview(id, evaluateResult);

        return new ReviewEvaluateResponse(
                result.reviewId(),
                result.status().name(),
                result.nextReviewDate() != null ? result.nextReviewDate().toString() : null
        );
    }
}