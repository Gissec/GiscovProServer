package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.CreateNewsSuccessResponse;

public interface NewsService {
    CreateNewsSuccessResponse createNews(NewsRequest request);
}
