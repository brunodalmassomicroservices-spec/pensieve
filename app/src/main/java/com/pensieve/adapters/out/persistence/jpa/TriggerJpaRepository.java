package com.pensieve.adapters.out.persistence.jpa;

import com.pensieve.adapters.out.persistence.entity.TriggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TriggerJpaRepository extends JpaRepository<TriggerEntity, UUID> {
}
