package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;
    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    @Autowired
    public FileSystemStorageService(StorageProperties properties) throws StorageException {
        if(properties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be empty");
        }
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }

    }

    @Override
    public void store(MultipartFile file) throws StorageException {

            logger.info(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {

            Path destinationFile = checkFile(file);

            Files.copy(inputStream,
                        destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                throw new StorageException(e.getMessage());
            }

    }

    private Path checkFile(MultipartFile file) throws StorageException {
        if (file.isEmpty())
            throw new StorageException("Failed to store empty file.");

        Path destinationFile = this.rootLocation.resolve(
                        Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()))
            throw new StorageException(
                    "Cannot store file outside current directory.");

        if (!Objects.requireNonNull(file.getContentType()).endsWith("vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            throw new StorageException(
                    "Cannot read file with extension different from xlsx format.");

        return destinationFile;
    }

    @Override
    public Stream<Path> loadAll() throws StorageException {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (Exception e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(!resource.exists()) {
                throw new StorageException("Could not find file: " + filename);
            }
            if(!resource.isReadable()) {
                throw new StorageException("Could not read file: " + filename);
            }

            return resource;

        }
        catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
