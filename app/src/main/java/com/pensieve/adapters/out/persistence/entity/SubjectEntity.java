package com.pensieve.adapters.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Getter
@Setter
public class SubjectEntity {

    @Id
    private UUID id;
    private UUID userId;
    private String title;
    private String colorHex;
    private LocalDateTime createdAt;

}