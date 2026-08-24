package com.pensieve.adapters.mappers;

import com.pensieve.adapters.out.persistence.entity.TriggerEntity;
import com.pensieve.adapters.out.persistence.entity.UsersEntity;
import com.pensieve.domain.Trigger;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public record TriggerMapper(EntityManager entityManager) {

    public TriggerEntity toEntity(Trigger domain) {
        if (domain == null)
            return null;

        var entity = new TriggerEntity();
        entity.setId(domain.getId());
        entity.setUsers(entityManager.getReference(UsersEntity.class, domain.getClientId()));
        entity.setSubject(domain.getSubject());
        entity.setTitle(domain.getTitle());
        entity.setNotes(domain.getNotes());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public Trigger toDomain(TriggerEntity entity) {
        if (entity == null)
            return null;

        var domain = new Trigger(entity.getUsers().getId(), entity.getSubject(), entity.getTitle(), entity.getNotes());
        setDomainField(domain, "id", entity.getId());
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
