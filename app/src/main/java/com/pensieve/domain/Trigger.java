package com.pensieve.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Trigger {
    private UUID id;
    private UUID subjectId;
    private String title;
    private String notes;
    private LocalDateTime createdAt;

    public Trigger(UUID subjectId, String title, String notes) {
        this.id = UUID.randomUUID();
        this.subjectId = subjectId;
        this.title = title;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }
}
