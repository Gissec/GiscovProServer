package com.example.GiscovAdvancedServer.repositories;

import com.example.GiscovAdvancedServer.models.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<LogEntity, Long> {
}
