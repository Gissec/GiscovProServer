package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.DTOs.response.CustomSuccessResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.services.FilesService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesServiceImpl implements FilesService {

    private final Path fileStorageLocation = Paths.get(Constants.STORAGE_DIR);

    public CustomSuccessResponse uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException(ServerErrorCodes.UNKNOWN);
        }
        createStorageDirectory();
        try {
            String originalFileName = file.getOriginalFilename();
            Path filePath = fileStorageLocation.resolve(originalFileName).normalize();
            file.transferTo(Path.of(filePath.toUri()));
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(Constants.PATH_FILE)
                    .path(originalFileName)
                    .toUriString();
            return new CustomSuccessResponse(fileDownloadUri);
        } catch (IOException e) {
            throw new CustomException(ServerErrorCodes.UNKNOWN);
        }
    }

    public ResponseEntity<UrlResource> downloadFile(String filename) {
        try {
            Path filePath = fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE,Constants.MULTIPART)
                        .body((UrlResource) resource);
            } else {
                throw new CustomException(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
            }
        } catch (IOException e) {
            throw new CustomException(ServerErrorCodes.UNKNOWN);
        }
    }

    private void createStorageDirectory() {
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (IOException e) {
                throw new CustomException(ServerErrorCodes.UNKNOWN);
            }
        }
    }
}
