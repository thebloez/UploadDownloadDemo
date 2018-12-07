package com.example.uploaddownload.service;

import com.example.uploaddownload.exception.FileStorageException;
import com.example.uploaddownload.exception.MyFileNotFoundException;
import com.example.uploaddownload.model.DBFile;
import com.example.uploaddownload.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public DBFile storeFile(MultipartFile file){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        DBFile dbFile = null;
        try {
            dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dbFile;
    }

    public DBFile getFile(String fileID){
        return fileRepository.findById(fileID)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileID));
    }

}
