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

import java.io.IOException;
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
    public ResponseEntity<Optional<Resource>> serveFile(@PathVariable String filename) {
        Optional<Resource> file = loadFile(filename);
        if (file.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.get().getFilename() + "\"").body(file);
    }
    
    private Optional<Resource> loadFile(String filename) throws StorageException {
        try{
            return Optional.of(storageService.loadAsResource(filename));
        } catch (StorageException e) {
             logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    @PostMapping("/files/insert")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes){
        logger.info(file.getOriginalFilename());
        saveAFile(file);
        redirectAttributes.addFlashAttribute("message", "File "+file.getOriginalFilename()+" uploaded successfully!");
        batchProcessing(file.getOriginalFilename());
        return "redirect:/files";
    }

    private void saveAFile(MultipartFile file){
        try {
            storageService.store(file);
        }catch (StorageException e){
            logger.error(e.getMessage());
        }
    }

    private void batchProcessing(String fileName){
        try {
            mainService.batch(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
