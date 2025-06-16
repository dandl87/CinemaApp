package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.exception.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();
    void store(MultipartFile file);
    Stream<Path> loadAll();
    Path load(String filename);
    Resource loadAsResource(String filename) throws StorageException;
    void deleteAll();
}
