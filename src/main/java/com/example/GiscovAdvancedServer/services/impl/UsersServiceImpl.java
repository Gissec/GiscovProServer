package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.request.PutUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.BaseSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PutUserResponse;
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
import org.springframework.transaction.annotation.Transactional;

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
        return userMapper.userEntityToUser(getCurrentUser());
    }

    @Transactional
    public PutUserResponse replaceUser(PutUserRequest putUserRequest) {
        UserEntity user = getCurrentUser();
        if (!putUserRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(putUserRequest.getEmail())) {
                throw new CustomException(ServerErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
            }
        }
        user.setAvatar(putUserRequest.getAvatar());
        user.setEmail(putUserRequest.getEmail());
        user.setRole(putUserRequest.getRole());
        user.setName(putUserRequest.getName());
        return userMapper.userEntityToPutUserResponse(user);
    }

    public BaseSuccessResponse deleteUser() {
        userRepository.deleteById(getCurrentUser().getId());
        return new BaseSuccessResponse();
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findById(((CustomUserDetails) authentication.getPrincipal()).getUserid())
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
    }
}
