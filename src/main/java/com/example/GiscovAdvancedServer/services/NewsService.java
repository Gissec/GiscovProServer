package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PageableResponse;
import java.util.List;
import java.util.UUID;

public interface NewsService {
    Long createNews(NewsRequest request);

    PageableResponse<List<GetNewsOutResponse>> getNews(Integer page, Integer perPage);

    PageableResponse<List<GetNewsOutResponse>> getUserNews(Integer page, Integer perPage,
                                                                                  UUID id);

    PageableResponse<List<GetNewsOutResponse>> findNews(String author, String keywords, Integer page, Integer perPage,
                                                        List<String> tags);

    void putNews(Long id, NewsRequest newsRequest);

    void deleteNews(Long id);
}
