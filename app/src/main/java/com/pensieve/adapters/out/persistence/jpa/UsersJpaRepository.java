package com.pensieve.adapters.out.persistence.jpa;

import com.pensieve.adapters.out.persistence.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersJpaRepository extends JpaRepository<UsersEntity, UUID> {
    boolean existsByEmail(String email);
}
