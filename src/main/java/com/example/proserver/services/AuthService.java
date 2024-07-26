package com.example.proserver.services;

import com.example.proserver.DTOs.request.RegisterUserRequest;
import com.example.proserver.DTOs.response.LoginUserResponse;
import com.example.proserver.mappers.UserMapper;
import com.example.proserver.models.UserEntity;
import com.example.proserver.repositories.UserRepository;
import com.example.proserver.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginUserResponse registrationRequest(RegisterUserRequest request) {
        UserEntity newUser = userMapper.userDtoToUserEntity(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);
        String token = jwtService.generateToken(newUser.getId().toString());
        LoginUserResponse response = userMapper.userEntityToLogin(newUser);
        response.setToken(token);
        return response;
    }
}
