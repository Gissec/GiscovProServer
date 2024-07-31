package com.example.proserver.services.impl;

import com.example.proserver.DTOs.request.AuthRequest;
import com.example.proserver.DTOs.request.RegisterUserRequest;
import com.example.proserver.DTOs.response.LoginUserResponse;
import com.example.proserver.error.CustomException;
import com.example.proserver.constans.ServerErrorCodes;
import com.example.proserver.mappers.UserMapper;
import com.example.proserver.models.UserEntity;
import com.example.proserver.repositories.AuthUserRepository;
import com.example.proserver.security.JwtService;
import com.example.proserver.services.AuthService;
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
            return userMapper.userEntityToLogin(user);
        } else {
            throw new CustomException(ServerErrorCodes.PASSWORD_NOT_VALID);
        }
    }
}
