package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.dto.request.PutUserRequest;
import com.example.GiscovAdvancedServer.dto.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.models.UserEntity;
import java.util.List;
import java.util.UUID;

public interface UsersService {

    List<PublicUserResponse> getAllUsers();

    PublicUserResponse getUserInfoById(UUID id);

    PublicUserResponse getUserInfo();

    PublicUserResponse replaceUser(PutUserRequest putUserRequest);

    void deleteUser();

    UserEntity getCurrentUser();

    UserEntity getUserById(UUID id);
}
