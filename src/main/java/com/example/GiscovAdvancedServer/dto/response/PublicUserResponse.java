package com.example.GiscovAdvancedServer.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class PublicUserResponse {

    private String avatar;

    private String email;

    private UUID id;

    private String name;

    private String role;
}
