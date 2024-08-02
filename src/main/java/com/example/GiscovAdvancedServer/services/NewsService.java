package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.CreateNewsSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.CustomSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PageableResponse;

import java.util.List;
import java.util.UUID;

public interface NewsService {
    CreateNewsSuccessResponse createNews(NewsRequest request);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(int page, int perPage);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNews(int page, int perPage, UUID id);

    PageableResponse<List<GetNewsOutResponse>> findNews(String author, String keywords, int page, int perPage,
                                                        List<String> tags);
}
