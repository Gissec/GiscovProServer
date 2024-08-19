package com.example.GiscovAdvancedServer.repositories;

import com.example.GiscovAdvancedServer.models.NewsEntity;
import com.example.GiscovAdvancedServer.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @EntityGraph(attributePaths = {"user", "tags"})
    Page<NewsEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT DISTINCT n FROM NewsEntity n " +
            "LEFT JOIN FETCH n.user " +
            "LEFT JOIN FETCH n.tags t " +
            "WHERE (:author IS NULL OR n.user.name ILIKE %:author%) " +
            "AND (:keywords IS NULL OR n.description ILIKE %:keywords%) " +
            "AND (:tags IS NULL OR t.title IN :tags)")
    @EntityGraph(value = "NewsEntity.full", type = EntityGraph.EntityGraphType.FETCH)
    Page<NewsEntity> findAllByAuthorAndKeywordsAndTags(
            @Param("author") String author,
            @Param("keywords") String keywords,
            @Param("tags") List<String> tags,
            Pageable pageable
    );

    Optional<NewsEntity> getNewsById(Long id);
}
