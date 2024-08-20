package com.example.GiscovAdvancedServer.DTOs.response;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class GetNewsOutResponse {

    private String description;

    private Long id;

    private String image;

    private List<TagResponse> tags;

    private String title;

    private UUID userId;

    private String username;
}
