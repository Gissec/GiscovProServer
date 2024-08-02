package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.CreateNewsSuccessResponse;
import com.example.GiscovAdvancedServer.mappers.NewsMapper;
import com.example.GiscovAdvancedServer.models.NewsEntity;
import com.example.GiscovAdvancedServer.models.TagsEntity;
import com.example.GiscovAdvancedServer.repositories.NewsRepository;
import com.example.GiscovAdvancedServer.services.NewsService;
import com.example.GiscovAdvancedServer.services.TagsService;
import com.example.GiscovAdvancedServer.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final TagsService tagsService;

    private final NewsMapper newsMapper;

    private final UsersService userService;

    public CreateNewsSuccessResponse createNews(NewsRequest request) {
        NewsEntity news = newsMapper.newsRequestToEntity(request);
        news.setUser(userService.getCurrentUser());
        List<TagsEntity> tags = tagsService.getAndSaveTags(request.getTags().stream()
                .parallel()
                .map(tag -> {
                    TagsEntity tagsEntity = new TagsEntity();
                    tagsEntity.setTitle(tag);
                    return tagsEntity;
                })
                .collect(Collectors.toList()));
        news.setTags(tags);
        newsRepository.save(news);
        return new CreateNewsSuccessResponse(news.getId());
    }
}
