package com.example.GiscovAdvancedServer.services;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {

    String uploadFile(MultipartFile file);

    UrlResource downloadFile(String filename);
}
