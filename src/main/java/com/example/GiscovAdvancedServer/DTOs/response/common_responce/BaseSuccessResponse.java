package com.example.GiscovAdvancedServer.DTOs.response.common_responce;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseSuccessResponse {

    private Integer statusCode = 0;

    private Boolean success = true;

    public BaseSuccessResponse(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
