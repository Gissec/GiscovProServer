package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.request.AuthRequest;
import com.example.GiscovAdvancedServer.DTOs.request.RegisterUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.LoginUserResponse;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.mappers.UserMapper;
import com.example.GiscovAdvancedServer.services.AuthService;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.AuthUserRepository;
import com.example.GiscovAdvancedServer.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;

    private final JwtService jwtService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public LoginUserResponse registrationRequest(RegisterUserRequest request) {
        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ServerErrorCodes.USER_ALREADY_EXISTS);
        }
        UserEntity newUser = userMapper.userDtoToUserEntity(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        authUserRepository.save(newUser);
        String token = jwtService.generateToken(newUser.getId().toString());
        LoginUserResponse response = userMapper.userEntityToLogin(newUser);
        response.setToken(token);
        return response;
    }

    public LoginUserResponse loginRequest(AuthRequest request) {
        UserEntity user = authUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user.getId().toString());
            LoginUserResponse response = userMapper.userEntityToLogin(user);
            response.setToken(token);
            return response;
        } else {
            throw new CustomException(ServerErrorCodes.PASSWORD_NOT_VALID);
        }
    }
}
