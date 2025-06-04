package com.delorenzo.Cinema.exception;

public class StorageFileInvalidExtensionException extends StorageFileException {
    public StorageFileInvalidExtensionException(String message) {

        super(message);
    }

    public StorageFileInvalidExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
