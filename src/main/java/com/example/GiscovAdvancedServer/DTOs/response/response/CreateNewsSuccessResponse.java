package com.example.GiscovAdvancedServer.DTOs.response.response;

import lombok.Data;

@Data
public class CreateNewsSuccessResponse {

    private Long id;

    private Integer statusCode = 0;

    private Boolean success = true;

    public CreateNewsSuccessResponse(Long id) {
        this.id = id;
    }
}
