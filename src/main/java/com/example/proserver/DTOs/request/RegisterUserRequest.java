package com.example.proserver.DTOs.request;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String avatar;
    private String email;
    private String name;
    private String password;
    private String role;
}
