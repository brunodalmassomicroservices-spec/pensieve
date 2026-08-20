package com.pensieve.adapters.mappers;

import java.util.UUID;

public record TriggerCreateRequest(UUID subject_id, String title, String notes) {
}
