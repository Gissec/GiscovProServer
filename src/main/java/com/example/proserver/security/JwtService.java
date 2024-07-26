package com.example.proserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Value("${token.expiration.time}")
    private long jwtExpirationTime;

    // Генерация токена
    public String generateToken(String uuid) {
        return "Bearer " + Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS512, jwtSigningKey)
                .compact();
    }

    // Извлечение идентификатора из токена
    public String getIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSigningKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Проверка просроченности токена
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSigningKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Проверка валидности токена
    public boolean isTokenValid(String token) {
        final String id = getIdFromToken(token);
        return (id != null && !isTokenExpired(token));
    }
}

