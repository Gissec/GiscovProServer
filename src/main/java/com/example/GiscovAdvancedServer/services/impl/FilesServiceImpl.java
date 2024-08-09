package com.example.GiscovAdvancedServer.services.impl;

import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import com.example.GiscovAdvancedServer.error.CustomException;
import com.example.GiscovAdvancedServer.services.FilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesServiceImpl implements FilesService {

    @Value("${storage.dir}")
    private String dir;

    private  Path fileStorageLocation = Paths.get(dir);;

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException(ServerErrorCodes.UNKNOWN);
        }
        createStorageDirectory();
        try {
            fileStorageLocation = Paths.get(dir);
            String originalFileName = file.getOriginalFilename();
            Path filePath = fileStorageLocation.resolve(originalFileName).normalize();
            file.transferTo(Path.of(filePath.toUri()));
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(Constants.PATH_FILE)
                    .path(originalFileName)
                    .toUriString();
            return fileDownloadUri;
        } catch (IOException e) {
            throw new CustomException(ServerErrorCodes.UNKNOWN);
        }
    }

    public UrlResource downloadFile(String filename) {
        try {
            fileStorageLocation = Paths.get(dir);
            Path filePath = fileStorageLocation.resolve(filename).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new CustomException(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
            }
        } catch (IOException e) {
            throw new CustomException(ServerErrorCodes.UNKNOWN);
        }
    }

    private void createStorageDirectory() {
        fileStorageLocation = Paths.get(dir);
        if (!fileStorageLocation.toFile().exists()) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (IOException e) {
                throw new CustomException(ServerErrorCodes.UNKNOWN);
            }
        }
    }
}
