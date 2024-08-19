package com.example.GiscovAdvancedServer.error;

import com.example.GiscovAdvancedServer.DTOs.response.common_responce.BaseSuccessResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(Constants.CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getOutputStream().println(objectMapper
                .writeValueAsString(new BaseSuccessResponse(ServerErrorCodes.UNAUTHORISED.getCode())));
    }
}
