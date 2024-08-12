package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PageableResponse;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
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
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final TagsService tagsService;

    private final NewsMapper newsMapper;

    private final UsersService userService;

    public Long createNews(NewsRequest request) {
        NewsEntity news = newsMapper.newsRequestToEntity(request);
        news.setUser(userService.getCurrentUser());
        Set<TagsEntity> tags = request.getTags().stream()
                        .map(tagString -> tagsService.findByTitle(tagString))
                        .collect(Collectors.toSet());
        news.setTags(tags);
        newsRepository.save(news);
        return news.getId();
    }

    public PageableResponse<List<GetNewsOutResponse>> getNews(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsPage = newsRepository.findAll(pageable);
        List<NewsEntity> test = newsPage.getContent();
        List<GetNewsOutResponse> news = newsPage.getContent().stream()
                .map(newsMapper::newsEntityToGetNewsOutResponse).toList();
        return new PageableResponse<>(news, Long.valueOf(news.size()));
    }

    public PageableResponse<List<GetNewsOutResponse>> getUserNews(Integer page, Integer perPage,
                                                                                         UUID id) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        UserEntity user = userService.getUserById(id);
        Page<NewsEntity> newsPage = newsRepository.findByUser(user, pageable);
        List<GetNewsOutResponse> news = newsPage.getContent().stream()
                .map(newsMapper::newsEntityToGetNewsOutResponse).toList();
        return new PageableResponse<>(news, Long.valueOf(news.size()));
    }

    public PageableResponse<List<GetNewsOutResponse>> findNews(String author, String keywords, Integer page,
                                                               Integer perPage, List<String> tags) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsPage = newsRepository.findAllByAuthorAndKeywordsAndTags(author, keywords, tags, pageable);
        List<GetNewsOutResponse> news = newsPage.getContent().stream()
                .map(newsMapper::newsEntityToGetNewsOutResponse).toList();
        return new PageableResponse<>(news, Long.valueOf(news.size()));
    }

    @Transactional
    public void putNews(Long id, NewsRequest newsRequest) {
        UserEntity user = userService.getCurrentUser();
        NewsEntity news = newsRepository.getNewsById(id)
                                        .orElseThrow(() -> new CustomException(ServerErrorCodes.NEWS_NOT_FOUND));
        if (user.getId().equals(news.getUser().getId())) {
            news = newsMapper.newsRequestToEntity(newsRequest);
            news.setUser(user);
            news.setId(id);
            Set<TagsEntity> tags = newsRequest.getTags().stream()
                    .map(tagString -> tagsService.findByTitle(tagString))
                    .collect(Collectors.toSet());
            news.setTags(tags);
            newsRepository.save(news);
            return;
        }
        throw new CustomException(ServerErrorCodes.NEWS_NOT_FOUND);
    }

    @Transactional
    public void deleteNews(Long id) {
        UserEntity user = userService.getCurrentUser();
        NewsEntity news = newsRepository.getNewsById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.NEWS_NOT_FOUND));
        if (user.getId().equals(news.getUser().getId())) {
            newsRepository.delete(news);
            //tagsService.deleteTags();
        } else {
            throw new CustomException(ServerErrorCodes.NEWS_NOT_FOUND);
        }
    }
}
