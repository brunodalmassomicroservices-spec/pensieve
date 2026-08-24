package com.pensieve.adapters.out.persistence.jpa;

import com.pensieve.adapters.out.persistence.entity.TriggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TriggerJpaRepository extends JpaRepository<TriggerEntity, UUID> {

    @Modifying
    @Query("delete from TriggerEntity t where t.users.id = :userId")
    void deleteAllByUserId(@Param("userId") UUID userId);
}
