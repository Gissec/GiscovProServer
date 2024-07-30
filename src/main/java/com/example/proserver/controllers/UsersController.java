package com.example.proserver.controllers;

import com.example.proserver.DTOs.response.CustomSuccessResponse;
import com.example.proserver.DTOs.response.PublicUserResponse;
import com.example.proserver.constans.Constants;
import com.example.proserver.constans.ValidationConstants;
import com.example.proserver.services.UsersService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> all() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(usersService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoById(@PathVariable
        @Size(min = 36, max = 36, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED)
        @Pattern(regexp = Constants.REGULAR_UUID, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(usersService.getUserInfoById(UUID.fromString(id))));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(usersService.getUserInfo()));
    }
}
