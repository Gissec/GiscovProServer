package com.example.proserver.services;

import com.example.proserver.DTOs.request.AuthRequest;
import com.example.proserver.DTOs.request.RegisterUserRequest;
import com.example.proserver.DTOs.response.LoginUserResponse;

public interface AuthService {
    LoginUserResponse registrationRequest(RegisterUserRequest request);

    LoginUserResponse loginRequest(AuthRequest request);
}
