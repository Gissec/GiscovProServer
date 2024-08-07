package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.models.TagsEntity;
import com.example.GiscovAdvancedServer.repositories.TagsRepository;
import com.example.GiscovAdvancedServer.services.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {

    private final TagsRepository tagsRepository;

    public Set<TagsEntity> getAndSaveTags(Set<TagsEntity> tags) {
        Set<TagsEntity> lte = new HashSet<>(tags.stream()
                .filter(tag -> !tagsRepository.existsByTitle(tag.getTitle()))
                .collect(Collectors.toSet()));
        if (lte.isEmpty()) {
            return tags;
        }
        tagsRepository.saveAll(lte);
        return tags;
    }

    public void deleteTags() {
        tagsRepository.deleteTags();
    }

    public TagsEntity findByTitle(String title) {
        return tagsRepository.findByTitle(title).orElseGet(() -> {
            TagsEntity tagsEntity = new TagsEntity();
            tagsEntity.setTitle(title);
            return tagsEntity;
        });
    }
}
