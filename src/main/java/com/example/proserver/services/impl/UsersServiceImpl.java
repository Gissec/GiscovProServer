package com.example.proserver.services.impl;

import com.example.proserver.DTOs.response.PublicUserViewResponse;
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

    public List<PublicUserViewResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().parallel().
                map(user -> userMapper.userEntityToUserView(user)).toList();
    }

    public PublicUserViewResponse getUserInfoById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
        return userMapper.userEntityToUserView(user);
    }

    public PublicUserViewResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal().toString());
        UUID userId = ((CustomUserDetails) authentication.getPrincipal()).getUserid();
        return userMapper.userEntityToUserView(userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND)));
    }
}
