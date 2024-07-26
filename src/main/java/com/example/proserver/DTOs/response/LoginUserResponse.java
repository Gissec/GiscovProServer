package com.example.proserver.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponse {

    private String avatar;

    private String email;

    private UUID id;

    private String name;

    private String role;

    private String token;
}
