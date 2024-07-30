package com.example.proserver.DTOs.request;

import com.example.proserver.constans.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthDtoRequest {
    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;

    @Size(min = 3, max = 25, message = ValidationConstants.PASSWORD_NOT_VALID)
    @NotBlank(message = ValidationConstants.PASSWORD_NOT_VALID)
    private String password;
}
