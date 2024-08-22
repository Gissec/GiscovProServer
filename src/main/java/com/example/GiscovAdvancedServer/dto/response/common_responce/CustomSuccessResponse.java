package com.example.GiscovAdvancedServer.dto.response.common_responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomSuccessResponse<T>{

    private Integer statusCode;

    private Boolean success;

    private T data;

    private List<Integer> codes;

    public CustomSuccessResponse(T data) {
        this.statusCode = 0;
        this.success = true;
        this.data = data;
    }

    public CustomSuccessResponse(Integer code, List<Integer> codes) {
        this.statusCode = code;
        this.success = true;
        this.codes = codes;
    }
}

