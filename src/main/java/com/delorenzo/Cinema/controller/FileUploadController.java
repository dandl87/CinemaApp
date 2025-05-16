package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.exception.StorageFileNotFoundException;
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
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files/insert")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes)
    {

        logger.info(file.getOriginalFilename());
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message", "File "+file.getOriginalFilename()+" uploaded successfully!");
        mainService.batch(file.getOriginalFilename());
        return "redirect:/files";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(){
        return ResponseEntity.notFound().build();
    }

}
