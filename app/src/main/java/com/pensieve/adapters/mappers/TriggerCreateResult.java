package com.pensieve.adapters.mappers;

import java.util.UUID;

public record TriggerCreateResult(UUID id, String title, int createdReviewsCount) {}