package com.example.uploaddownload.exception;

/**
 * Created by thebloez on 04/12/18.
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
