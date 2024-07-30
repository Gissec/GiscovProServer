package com.example.proserver.DTOs.response;

import lombok.Data;
import java.util.UUID;

@Data
public class PublicUserViewResponse {

    private String avatar;

    private String email;

    private UUID id;

    private String name;

    private String role;
}
