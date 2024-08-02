package com.example.GiscovAdvancedServer.repositories;

import com.example.GiscovAdvancedServer.models.NewsEntity;
import com.example.GiscovAdvancedServer.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    @EntityGraph(attributePaths = {"user", "tags"})
    Page<NewsEntity> findByUser(UserEntity user, Pageable pageable);
}
