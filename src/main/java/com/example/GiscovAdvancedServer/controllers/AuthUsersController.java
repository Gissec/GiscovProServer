package com.example.GiscovAdvancedServer.controllers;

import com.example.GiscovAdvancedServer.dto.request.AuthRequest;
import com.example.GiscovAdvancedServer.dto.request.RegisterUserRequest;
import com.example.GiscovAdvancedServer.dto.response.common_responce.CustomSuccessResponse;
import com.example.GiscovAdvancedServer.dto.response.LoginUserResponse;
import com.example.GiscovAdvancedServer.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthUsersController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> registrationRequest(@RequestBody
                                                                  @Valid RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authService.registrationRequest(registerUserRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> loginRequest(@RequestBody
                                                                  @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authService.loginRequest(authRequest)));
    }
}
