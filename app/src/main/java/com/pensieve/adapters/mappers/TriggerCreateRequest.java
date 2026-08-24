package com.pensieve.adapters.mappers;

public record TriggerCreateRequest(
        String subject,
        String title,
        String notes) {
}
