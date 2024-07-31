package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.response.PublicUserResponse;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    List<PublicUserResponse> getAllUsers();

    PublicUserResponse getUserInfoById(UUID id);

    PublicUserResponse getUserInfo();
}
