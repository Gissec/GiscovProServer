package com.example.proserver.controllers;

import com.example.proserver.DTOs.request.AuthRequest;
import com.example.proserver.DTOs.request.RegisterUserRequest;
import com.example.proserver.DTOs.response.CustomSuccessResponse;
import com.example.proserver.DTOs.response.LoginUserResponse;
import com.example.proserver.services.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthUsersController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> registrationRequest(@RequestBody
                                                                  @Valid RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authServiceImpl.registrationRequest(registerUserRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> loginRequest(@RequestBody
                                                                                 @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authServiceImpl.loginRequest(authRequest)));
    }
}
