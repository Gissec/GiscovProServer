package com.example.GiscovAdvancedServer.mappers;

import com.example.GiscovAdvancedServer.dto.request.NewsRequest;
import com.example.GiscovAdvancedServer.dto.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.dto.response.TagResponse;
import com.example.GiscovAdvancedServer.models.NewsEntity;
import com.example.GiscovAdvancedServer.models.TagsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "tags")
    NewsEntity newsRequestToEntity(NewsRequest newsRequest);

    @Mapping(target = "userId", expression = "java(news.getUser().getId())")
    @Mapping(target = "username", expression = "java(news.getUser().getName())")
    @Mapping(target = "tags", expression = "java(mapTags(news.getTags()))")
    GetNewsOutResponse newsEntityToGetNewsOutResponse(NewsEntity news);

    default TagResponse map(TagsEntity value) {
        if (value == null) {
            return null;
        }
        TagResponse tagResponse = new TagResponse();
        tagResponse.setTitle(value.getTitle());
        tagResponse.setId(value.getId());
        return tagResponse;
    }

    default List<TagResponse> mapTags(Set<TagsEntity> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
