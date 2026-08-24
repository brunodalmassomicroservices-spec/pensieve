package com.pensieve.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Trigger {
    private UUID id;
    private UUID clientId;
    private String subject;
    private String title;
    private String notes;
    private LocalDateTime createdAt;

    public Trigger(UUID clientId, String subject, String title, String notes) {
        this.id = UUID.randomUUID();
        this.clientId = clientId;
        this.subject = subject;
        this.title = title;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }
}
