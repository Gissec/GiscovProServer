package com.example.GiscovAdvancedServer.dto.request;

import com.example.GiscovAdvancedServer.constans.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotBlank(message = ValidationConstants.USER_AVATAR_NOT_NULL)
    private String avatar;

    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;

    @Size(min = 3, max = 25, message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    private String name;

    @Size(min = 3, max = 25, message = ValidationConstants.USER_PASSWORD_NOT_VALID)
    @NotNull(message = ValidationConstants.USER_PASSWORD_NULL)
    private String password;

    @Size(min = 3, max = 25, message = ValidationConstants.UNKNOWN)
    @NotBlank(message = ValidationConstants.USER_ROLE_NOT_NULL)
    private String role;
}
