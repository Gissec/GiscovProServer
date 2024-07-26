package com.example.proserver.repositories;

import com.example.proserver.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface  UserRepository extends JpaRepository<UserEntity, UUID> {
     Optional<UserEntity> findById(UUID id);
}
