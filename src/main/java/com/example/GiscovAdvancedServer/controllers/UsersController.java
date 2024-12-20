package com.example.GiscovAdvancedServer.controllers;

import com.example.GiscovAdvancedServer.dto.request.PutUserRequest;
import com.example.GiscovAdvancedServer.dto.response.common_responce.BaseSuccessResponse;
import com.example.GiscovAdvancedServer.dto.response.common_responce.CustomSuccessResponse;
import com.example.GiscovAdvancedServer.dto.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ValidationConstants;
import com.example.GiscovAdvancedServer.services.UsersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> all() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(usersService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoById(@PathVariable
            @Size(min = 36, max = 36, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED)
            @Pattern(regexp = Constants.REGULAR_UUID,
                    message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(usersService.getUserInfoById(UUID.fromString(id))));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        return ResponseEntity.ok(new CustomSuccessResponse<>(usersService.getUserInfo()));
    }

    @PutMapping()
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> replaceUser(@Valid
            @RequestBody PutUserRequest request) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(usersService.replaceUser(request)));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteUser() {
        usersService.deleteUser();
        return ResponseEntity.ok(new BaseSuccessResponse());
    }
}
