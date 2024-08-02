package com.example.GiscovAdvancedServer.repositories;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<TagsEntity, Long> {
    Boolean existsByTitle(String name);
}
