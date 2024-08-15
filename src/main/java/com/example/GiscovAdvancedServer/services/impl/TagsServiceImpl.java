package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import com.example.GiscovAdvancedServer.repositories.TagsRepository;
import com.example.GiscovAdvancedServer.services.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {

    private final TagsRepository tagsRepository;

    public void deleteTags() {
        tagsRepository.deleteTags();
    }

    public Set<TagsEntity> findOrCreateTags(List<String> titles) {
        List<TagsEntity> existingTags = tagsRepository.findByTitleIn(titles);
        Set<String> existingTitles = existingTags.stream()
                .map(TagsEntity::getTitle)
                .collect(Collectors.toSet());
        Set<TagsEntity> newTags = titles.stream()
                .filter(title -> !existingTitles.contains(title))
                .map(title -> {
                    TagsEntity newTag = new TagsEntity();
                    newTag.setTitle(title);
                    return newTag;
                })
                .collect(Collectors.toSet());
        tagsRepository.saveAll(newTags);
        existingTags.addAll(newTags);
        return new HashSet<>(existingTags);
    }
}
