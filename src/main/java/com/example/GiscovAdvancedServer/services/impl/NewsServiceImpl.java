package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.CreateNewsSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.CustomSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PageableResponse;
import com.example.GiscovAdvancedServer.mappers.NewsMapper;
import com.example.GiscovAdvancedServer.models.NewsEntity;
import com.example.GiscovAdvancedServer.models.TagsEntity;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.NewsRepository;
import com.example.GiscovAdvancedServer.services.NewsService;
import com.example.GiscovAdvancedServer.services.TagsService;
import com.example.GiscovAdvancedServer.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
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

    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsPage = newsRepository.findAll(pageable);
        List<GetNewsOutResponse> news = newsPage.getContent().stream()
                .map(newsMapper::newsEntityToGetNewsOutResponse).toList();
        return new CustomSuccessResponse<>(new PageableResponse<>(news, Long.valueOf(news.size())));
    }

    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNews(int page, int perPage, UUID id) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        UserEntity user = userService.getUserById(id);
        Page<NewsEntity> newsPage = newsRepository.findByUser(user, pageable);
        List<GetNewsOutResponse> news = newsPage.getContent().stream()
                .map(newsMapper::newsEntityToGetNewsOutResponse).toList();
        return new CustomSuccessResponse<>(new PageableResponse<>(news, Long.valueOf(news.size())));
    }
}
