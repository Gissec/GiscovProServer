package com.example.GiscovAdvancedServer.repositories;

import com.example.GiscovAdvancedServer.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface  UserRepository extends JpaRepository<UserEntity, UUID> {
     Optional<UserEntity> findById(UUID id);

     Boolean existsByEmail(String email);
}
