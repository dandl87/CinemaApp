package com.delorenzo.Cinema.exception;

public class StorageFileException extends StorageException {
    public StorageFileException(String message) {
        super(message);
    }

    public StorageFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
