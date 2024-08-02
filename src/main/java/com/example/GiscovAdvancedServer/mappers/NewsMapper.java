package com.example.GiscovAdvancedServer.mappers;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.models.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "tags")
    NewsEntity newsRequestToEntity(NewsRequest newsRequest);

    @Mapping(target = "userId", expression = "java(news.getUser().getId())")
    @Mapping(target = "username", expression = "java(news.getUser().getName())")
    GetNewsOutResponse newsEntityToGetNewsOutResponse(NewsEntity news);
}
