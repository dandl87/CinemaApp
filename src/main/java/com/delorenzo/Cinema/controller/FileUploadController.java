package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.service.WeeklyProcessingService;
import com.delorenzo.Cinema.service.StorageService;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final WeeklyProcessingService mainService;
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    public FileUploadController(StorageService storageService, WeeklyProcessingService mainService) {
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
            RedirectAttributes redirectAttributes) throws IOException, RuntimeException {

        logger.info(file.getOriginalFilename());
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message", "File "+file.getOriginalFilename()+" uploaded successfully!");
        mainService.batch(file.getOriginalFilename());
        return "redirect:/files";
    }

    @ExceptionHandler(NotOfficeXmlFileException.class)
    public String handleNotAValidFileError(Exception ex, Model model){
        model.addAttribute("type", "File Error");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }


}
