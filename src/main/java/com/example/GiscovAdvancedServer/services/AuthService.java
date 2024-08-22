package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.dto.request.AuthRequest;
import com.example.GiscovAdvancedServer.dto.request.RegisterUserRequest;
import com.example.GiscovAdvancedServer.dto.response.LoginUserResponse;

public interface AuthService {

    LoginUserResponse registrationRequest(RegisterUserRequest request);

    LoginUserResponse loginRequest(AuthRequest request);
}
