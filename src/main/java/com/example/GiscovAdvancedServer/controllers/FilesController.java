package com.example.GiscovAdvancedServer.controllers;

import com.example.GiscovAdvancedServer.DTOs.response.common_responce.CustomSuccessResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ValidationConstants;
import com.example.GiscovAdvancedServer.services.FilesService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Validated
@RequiredArgsConstructor
@CrossOrigin("*")
public class FilesController {

    private final FilesService filesService;

    @PostMapping("/uploadFile")
    public ResponseEntity<CustomSuccessResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok((new CustomSuccessResponse<>(filesService.uploadFile(file))));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable
                      @NotBlank(message = ValidationConstants.EXCEPTION_HANDLER_NOT_PROVIDED) String fileName) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Constants.MULTIPART)
                .body(filesService.downloadFile(fileName));
    }
}
