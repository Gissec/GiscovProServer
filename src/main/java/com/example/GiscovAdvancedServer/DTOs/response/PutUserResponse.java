package com.example.GiscovAdvancedServer.DTOs.response;

import lombok.Data;
import java.util.UUID;

@Data
public class PutUserResponse {

    private String avatar;

    private String email;

    private UUID id;

    private String name;

    private String role;
}