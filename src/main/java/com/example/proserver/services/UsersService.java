package com.example.proserver.services;

import com.example.proserver.DTOs.response.PublicUserViewResponse;
import java.util.List;
import java.util.UUID;

public interface UsersService {
    List<PublicUserViewResponse> getAllUsers();

    PublicUserViewResponse getUserInfoById(UUID id);

    PublicUserViewResponse getUserInfo();
}
