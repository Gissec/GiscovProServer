package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import java.util.List;

public interface TagsService {
    List<TagsEntity> getAndSaveTags(List<TagsEntity> news);
}
