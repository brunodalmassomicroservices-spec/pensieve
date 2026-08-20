package com.pensieve.adapters.in;


import com.pensieve.adapters.mappers.ReviewEvaluateRequest;
import com.pensieve.adapters.mappers.ReviewEvaluateResponse;
import com.pensieve.adapters.mappers.TriggerCreateRequest;
import com.pensieve.adapters.mappers.TriggerCreateResponse;
import com.pensieve.application.ReviewService;
import com.pensieve.application.TriggerService;
import com.pensieve.application.records.PendingReviewDto;
import com.pensieve.domain.EvaluateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Revisões", description = "Criação de gatilhos e gestão das revisões espaçadas.")
public class PensieveController {

    private final TriggerService triggerService;
    private final ReviewService reviewService;

    public PensieveController(TriggerService triggerService, ReviewService reviewService) {
        this.triggerService = triggerService;
        this.reviewService = reviewService;
    }

    @PostMapping("/triggers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cria um gatilho de estudo",
            description = "Cria o gatilho e agenda automaticamente revisões para D+1, D+7, D+30 e D+180.")
    @ApiResponse(responseCode = "201", description = "Gatilho criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos", content = @Content)
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
    @Operation(
            summary = "Lista as revisões pendentes de hoje",
            description = "Retorna revisões agendadas para hoje ou para datas anteriores que ainda estejam pendentes.")
    @ApiResponse(responseCode = "200", description = "Revisões retornadas com sucesso")
    public ReviewsTodayResponse getTodaysReviews() {
        var items = reviewService.getTodaysReviews();
        return new ReviewsTodayResponse(items.size(), items); // Retorna a lista e o total[cite: 2]
    }

    @PostMapping("/reviews/{id}/evaluate")
    @Operation(summary = "Registra a avaliação de uma revisão")
    @ApiResponse(responseCode = "200", description = "Avaliação registrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Resultado de avaliação inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Revisão não encontrada", content = @Content)
    public ReviewEvaluateResponse evaluateReview(
            @Parameter(description = "Identificador da revisão", required = true,
                    schema = @Schema(format = "uuid")) @PathVariable UUID id,
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
