package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.exception.StorageException;
import com.delorenzo.Cinema.service.MainService;
import com.delorenzo.Cinema.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final MainService mainService;
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    public FileUploadController(StorageService storageService, MainService mainService) {
        this.storageService = storageService;
        this.mainService = mainService;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<String> serveFile(@PathVariable String filename) {
        Optional<Resource> file = loadFile(filename);
        if (file.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.get().getFilename() + "\"").body(file.get().getFilename());
    }

    private Optional<Resource> loadFile(String filename) {
        try{
            return Optional.of(storageService.loadAsResource(filename));
        } catch (Exception e) {
             logger.error(e.getMessage());
             return Optional.empty();
        }
    }

    @PostMapping("/files/insert")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws StorageException {
        logger.info(file.getOriginalFilename());
        saveAFile(file);
        redirectAttributes.addFlashAttribute("message", "File "+file.getOriginalFilename()+" uploaded successfully!");
        batchProcessing(file.getOriginalFilename());
        return "redirect:/files";
    }

    private void saveAFile(MultipartFile file) throws StorageException {
        try {
            storageService.store(file);
        }catch (StorageException e){
            logger.error(e.getMessage());
            throw new StorageException(e.getMessage());
        }
    }

    private void batchProcessing(String fileName) throws StorageException {
        try {
            mainService.batch(fileName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new StorageException(e.getMessage());
        }
    }




}
