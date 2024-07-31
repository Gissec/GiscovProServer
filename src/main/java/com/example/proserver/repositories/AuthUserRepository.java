package com.example.proserver.repositories;

import com.example.proserver.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<UserEntity, UUID> {
    Boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
