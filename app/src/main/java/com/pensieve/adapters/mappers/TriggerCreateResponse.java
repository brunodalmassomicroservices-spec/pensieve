package com.pensieve.adapters.mappers;

import java.util.UUID;

public record TriggerCreateResponse(UUID id, String title, int created_reviews_count) {
}
