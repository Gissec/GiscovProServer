package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import java.util.Set;

public interface TagsService {
    Set<TagsEntity> getAndSaveTags(Set<TagsEntity> tags);

    void deleteTags();

    TagsEntity findByTitle(String title);
}
