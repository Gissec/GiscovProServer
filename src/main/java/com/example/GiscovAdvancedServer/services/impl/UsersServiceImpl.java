package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.request.PutUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.mappers.UserMapper;
import com.example.GiscovAdvancedServer.models.UserEntity;
import com.example.GiscovAdvancedServer.repositories.UserRepository;
import com.example.GiscovAdvancedServer.services.TagsService;
import com.example.GiscovAdvancedServer.services.UsersService;
import lombok.RequiredArgsConstructor;
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

    private final TagsService tagsService;

    public List<PublicUserResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return userMapper.usersEntityToUserList(users);
    }

    public PublicUserResponse getUserInfoById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
        return userMapper.userEntityToUser(user);
    }

    public PublicUserResponse getUserInfo() {
        UserEntity user = getCurrentUser();
        PublicUserResponse userResponse = userMapper.userEntityToUser(user);
        return userResponse;
    }

    @Transactional
    public PublicUserResponse replaceUser(PutUserRequest putUserRequest) {
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
        return userMapper.userEntityToUser(user);
    }

    @Transactional
    public void deleteUser() {
        userRepository.deleteById(getCurrentUser().getId());
        tagsService.deleteTags();
    }

    public UserEntity getCurrentUser() {
        String id = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        UserEntity user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
        return user;
    }

    public UserEntity getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
    }
}
