package com.example.uploaddownload.controller;

import com.example.uploaddownload.model.DBFile;
import com.example.uploaddownload.payload.UploadFileResponse;
import com.example.uploaddownload.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DBFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBFileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadfile")
    public UploadFileResponse uploadFile(@RequestParam("file")MultipartFile file){
        DBFile dbFile = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFromDB/")
                .path(dbFile.getId())
                .toUriString();
        return new UploadFileResponse(dbFile.getFilename(),
                fileDownloadUri,
                dbFile.getFiletype(),
                file.getSize());
    }

    @PostMapping("/uploadmultiple")
    public List<UploadFileResponse> uploadMultipleFiles (@RequestParam("files") MultipartFile[] files){
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFromDB/{fileId}")
    public ResponseEntity<Resource> getFile (@PathVariable("fileId") String id) {
        // Load from Database
        DBFile dbFile = fileService.getFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "Attachment; filename=\"" + dbFile.getFilename() +
                        "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
}
