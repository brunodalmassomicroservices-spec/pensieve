package com.pensieve.adapters.mappers;

import com.pensieve.adapters.out.persistence.entity.ReviewEntity;
import com.pensieve.adapters.out.persistence.entity.TriggerEntity;
import com.pensieve.domain.Review;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public record ReviewMapper(EntityManager entityManager) {
    public ReviewEntity toEntity(Review domain) {
        if (domain == null) return null;
        var entity = new ReviewEntity();
        entity.setId(domain.getId());
        entity.setTrigger(entityManager.getReference(TriggerEntity.class, domain.getTriggerId()));
        entity.setStatus(domain.getStatus());
        entity.setIntervalDays(domain.getIntervalDays());
        entity.setScheduledFor(domain.getScheduledFor());
        entity.setCompletedAt(domain.getCompletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public Review toDomain(ReviewEntity entity) {
        if (entity == null) return null;
        var domain = new Review(entity.getTrigger().getId(), entity.getIntervalDays(), entity.getScheduledFor());
        setDomainField(domain, "id", entity.getId());
        setDomainField(domain, "status", entity.getStatus());
        setDomainField(domain, "completedAt", entity.getCompletedAt());
        setDomainField(domain, "createdAt", entity.getCreatedAt());
        return domain;
    }

    private void setDomainField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao mapear", e);
        }
    }
}