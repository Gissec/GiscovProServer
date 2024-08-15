package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import java.util.List;
import java.util.Set;

public interface TagsService {

    void deleteTags();

    Set<TagsEntity> findOrCreateTags(List<String> titles);
}
