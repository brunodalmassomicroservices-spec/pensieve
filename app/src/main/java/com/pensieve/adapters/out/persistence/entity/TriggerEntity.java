package com.pensieve.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "triggers")
@Getter
@Setter
public class TriggerEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity users;

    private String subject;
    private String title;
    private String notes;
    private LocalDateTime createdAt;
}
