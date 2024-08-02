package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.request.PutUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.BaseSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PutUserResponse;
import com.example.GiscovAdvancedServer.models.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    List<PublicUserResponse> getAllUsers();

    PublicUserResponse getUserInfoById(UUID id);

    PublicUserResponse getUserInfo();

    PutUserResponse replaceUser(PutUserRequest putUserRequest);

    BaseSuccessResponse deleteUser();

    UserEntity getCurrentUser();
}
