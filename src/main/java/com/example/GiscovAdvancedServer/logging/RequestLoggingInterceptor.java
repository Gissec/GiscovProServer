package com.example.GiscovAdvancedServer.logging;

import com.example.GiscovAdvancedServer.models.LogEntity;
import com.example.GiscovAdvancedServer.repositories.RequestLogRepository;
import com.example.GiscovAdvancedServer.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final RequestLogRepository requestLogRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogEntity log = new LogEntity();
        log.setUri(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setMethodType(((HandlerMethod) handler).getMethod().getName());
        log.setResponseCode(response.getStatus());
        log.setTimestamp(LocalDateTime.now());
        //request.getRemoteUser();
        CustomUserDetails userDetails = (CustomUserDetails) request.getUserPrincipal();
        if (userDetails != null) {
            log.setAuthorId(userDetails.getUsername());
        } else {
            log.setAuthorId(null);
        }
        if (ex != null) {
            log.setErrorMessage(ex.getMessage());
        }
        requestLogRepository.save(log);
    }
}
