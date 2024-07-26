package com.example.proserver.configuration;

import com.example.proserver.error.CustomEntryPoint;
import com.example.proserver.security.CustomUserDetailsService;
import com.example.proserver.security.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final Filter customFilter; // Ваш фильтр
    private final CustomEntryPoint customEntryPoint; // Ваша точка входа для авторизации

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Отключение CSRF
                .cors(AbstractHttpConfigurer::disable) // Отключение CORS
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Разрешение Pre-Flight запросов
                                .requestMatchers("/v1/auth/**").permitAll() // Доступ без аутентификации
                                .anyRequest().authenticated()  // Разрешить все остальные запросы только с аунтификацией
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Сессии не сохраняются
                )
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class) // Фильтр перед обработкой запросов
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customEntryPoint) // Точка входа для авторизации
                );

        return http.build();
    }
}

