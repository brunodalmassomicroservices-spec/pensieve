package com.pensieve.adapters.out.persistence;

import com.pensieve.adapters.out.persistence.jpa.TriggerJpaRepository;
import com.pensieve.adapters.mappers.TriggerMapper;
import com.pensieve.application.TriggerRepository;
import com.pensieve.domain.Trigger;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TriggerRepositoryAdapter implements TriggerRepository {

    private final TriggerJpaRepository jpaRepository;
    private final TriggerMapper mapper;

    // A injeção funciona perfeitamente, pois o mapper é um @Component
    public TriggerRepositoryAdapter(TriggerJpaRepository jpaRepository, TriggerMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Trigger trigger) {
        var entity = mapper.toEntity(trigger);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Trigger> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}