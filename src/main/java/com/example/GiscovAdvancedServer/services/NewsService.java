package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.*;

import java.util.List;
import java.util.UUID;

public interface NewsService {
    CreateNewsSuccessResponse createNews(NewsRequest request);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(Integer page, Integer perPage);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNews(Integer page, Integer perPage,
                                                                                  UUID id);

    PageableResponse<List<GetNewsOutResponse>> findNews(String author, String keywords, Integer page, Integer perPage,
                                                        List<String> tags);

    BaseSuccessResponse putNews(Long id, NewsRequest newsRequest);
}
