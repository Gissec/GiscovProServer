package com.example.proserver.services;

import com.example.proserver.DTOs.response.PublicUserResponse;
import java.util.List;
import java.util.UUID;

public interface UsersService {
    List<PublicUserResponse> getAllUsers();

    PublicUserResponse getUserInfoById(UUID id);

    PublicUserResponse getUserInfo();
}
