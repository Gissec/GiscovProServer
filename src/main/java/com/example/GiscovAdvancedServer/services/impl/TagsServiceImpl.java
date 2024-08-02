package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import com.example.GiscovAdvancedServer.repositories.TagsRepository;
import com.example.GiscovAdvancedServer.services.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {

    private final TagsRepository tagsRepository;

    public List<TagsEntity> getAndSaveTags(List<TagsEntity> tags) {
        List<TagsEntity> lte = tags.stream()
                .filter(tag ->!tagsRepository.existsByTitle(tag.getTitle()))
                .toList();
        if (lte.isEmpty()) {
            return tags;
        }
        return tagsRepository.saveAll(lte);
    }
}
