package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.security.CustomUserDetails;
import com.example.GiscovAdvancedServer.mappers.UserMapper;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.UserRepository;
import com.example.GiscovAdvancedServer.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<PublicUserResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().parallel().
                map(user -> userMapper.userEntityToUser(user)).toList();
    }

    public PublicUserResponse getUserInfoById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
        return userMapper.userEntityToUser(user);
    }

    public PublicUserResponse getUserInfo() {
        return userMapper.userEntityToUser(userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND)));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getUserid();
    }
}
