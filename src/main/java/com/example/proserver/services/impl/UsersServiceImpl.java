package com.example.proserver.services.impl;

import com.example.proserver.DTOs.response.PublicUserResponse;
import com.example.proserver.constans.ServerErrorCodes;
import com.example.proserver.error.CustomException;
import com.example.proserver.mappers.UserMapper;
import com.example.proserver.models.UserEntity;
import com.example.proserver.repositories.UserRepository;
import com.example.proserver.security.CustomUserDetails;
import com.example.proserver.services.UsersService;
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
                map(user -> userMapper.userEntityToUserView(user)).toList();
    }

    public PublicUserResponse getUserInfoById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
        return userMapper.userEntityToUserView(user);
    }

    public PublicUserResponse getUserInfo() {
        return userMapper.userEntityToUserView(userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND)));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getUserid();
    }
}
