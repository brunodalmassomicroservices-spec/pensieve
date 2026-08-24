package com.pensieve.adapters.mappers;

import java.util.UUID;

public record TriggerCreateResponse(
        UUID id,
        UUID userId,
        String subject,
        String title) {
}
