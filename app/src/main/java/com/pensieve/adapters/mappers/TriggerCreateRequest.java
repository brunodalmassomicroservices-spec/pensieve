package com.pensieve.adapters.mappers;

public record TriggerCreateRequest(
        String clientId,
        String subject,
        String title,
        String notes) {
}
