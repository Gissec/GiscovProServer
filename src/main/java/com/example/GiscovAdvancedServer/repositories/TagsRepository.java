package com.example.GiscovAdvancedServer.repositories;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface TagsRepository extends JpaRepository<TagsEntity, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM news.tags_entity WHERE id NOT IN (SELECT DISTINCT tag_id FROM news.news_tags)",
            nativeQuery = true)
    void deleteTags();

    List<TagsEntity> findByTitleIn(List<String> titles);
}
