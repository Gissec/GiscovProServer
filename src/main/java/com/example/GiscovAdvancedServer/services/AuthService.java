package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.request.AuthRequest;
import com.example.GiscovAdvancedServer.DTOs.request.RegisterUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.response.LoginUserResponse;

public interface AuthService {

    LoginUserResponse registrationRequest(RegisterUserRequest request);

    LoginUserResponse loginRequest(AuthRequest request);
}
