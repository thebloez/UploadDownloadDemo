package com.example.uploaddownload.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by thebloez on 04/12/18.
 */

@ConfigurationProperties(prefix = "file")
public class FileStorageProperty {

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
