package com.pensieve.adapters.mappers;

import java.util.UUID;

public record ReviewEvaluateResponse(UUID review_id, String status, String next_review_date) {}
