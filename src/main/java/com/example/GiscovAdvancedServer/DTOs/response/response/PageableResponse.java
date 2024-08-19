package com.example.GiscovAdvancedServer.DTOs.response.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageableResponse<T> {

    private T content;

    private Long numberOfElements;
}
