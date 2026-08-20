package com.pensieve.application;

import com.pensieve.domain.Trigger;

import java.util.Optional;
import java.util.UUID;

public interface TriggerRepository {
    void save(Trigger trigger);

    Optional<Trigger> findById(UUID id);
}
