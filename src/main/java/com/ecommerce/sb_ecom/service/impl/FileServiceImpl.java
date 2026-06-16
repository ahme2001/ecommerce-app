package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    public String uploadImage(String path, MultipartFile image) throws IOException {

        String originalFilename = image.getOriginalFilename();
        String randomName = UUID.randomUUID().toString();

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = randomName + extension;

        Path uploadDir = Paths.get(path);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path targetFile = uploadDir.resolve(fileName);

        Files.copy(image.getInputStream(), targetFile);

        return fileName;
    }
}
