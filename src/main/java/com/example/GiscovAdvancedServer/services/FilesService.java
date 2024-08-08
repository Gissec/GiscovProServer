package com.example.GiscovAdvancedServer.services;

import com.example.GiscovAdvancedServer.DTOs.response.CustomSuccessResponse;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    CustomSuccessResponse uploadFile(MultipartFile file);

    ResponseEntity<UrlResource> downloadFile(String filename);
}
