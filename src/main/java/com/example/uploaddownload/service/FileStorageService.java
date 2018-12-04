package com.example.uploaddownload.service;

import com.example.uploaddownload.exception.FileStorageException;
import com.example.uploaddownload.exception.MyFileNotFoundException;
import com.example.uploaddownload.property.FileStorageProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by thebloez on 04/12/18.
 */
@Service
public class FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperty property) {

        this.fileStorageLocation = Paths.get(property.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex){
            throw new FileStorageException("Could not create the directory where the uploaded will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file){
        // normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // check if the file's name contains invalid character
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence." + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName){
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File Not Found" + fileName);
            }

        } catch (MalformedURLException e) {
            throw new MyFileNotFoundException("File Not Found" + fileName, e);
        }
    }
}
