package com.example.proserver.services.impl;

import com.example.proserver.DTOs.request.AuthRequest;
import com.example.proserver.DTOs.request.RegisterUserRequest;
import com.example.proserver.DTOs.response.LoginUserResponse;

public interface AuthServiceImpl {
    LoginUserResponse registrationRequest(RegisterUserRequest request);

    LoginUserResponse loginRequest(AuthRequest request);
}
