package com.example.GiscovAdvancedServer.logging;

import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.models.LogEntity;
import com.example.GiscovAdvancedServer.repositories.RequestLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.security.Principal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final RequestLogRepository requestLogRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        LogEntity log = new LogEntity();
        log.setUri(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setMethodType(((HandlerMethod) handler).getMethod().getName());
        log.setResponseCode(response.getStatus());
        log.setTimestamp(LocalDateTime.now());
        Principal user = request.getUserPrincipal();
        if (user != null) {
            log.setAuthorId(user.getName());
        } else {
            log.setAuthorId(Constants.ANONIMUS);
        }
        String errorMessage = response.getHeader(Constants.NAME_ERROR);
        if (errorMessage != null) {
            log.setErrorMessage(errorMessage);
        } else {
            log.setErrorMessage(Constants.NO_ERROR);
        }
        requestLogRepository.save(log);
    }
}
