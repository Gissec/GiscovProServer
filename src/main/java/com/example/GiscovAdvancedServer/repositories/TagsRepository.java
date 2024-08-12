package com.example.GiscovAdvancedServer.repositories;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface TagsRepository extends JpaRepository<TagsEntity, Long> {
    Boolean existsByTitle(String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM news.tags_entity WHERE id NOT IN (SELECT DISTINCT tag_id FROM news.news_tags)",
            nativeQuery = true)
    void deleteTags();

    Optional<TagsEntity> findByTitle(String title);
}
