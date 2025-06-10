package com.delorenzo.Cinema.exception;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotOfficeXmlFileException.class)
    public String handleNotAValidFileError(Exception ex, Model model){
        model.addAttribute("type", "File Error");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(StorageException.class)
    public String handleStorageFileError(Exception ex, Model model){
        model.addAttribute("type", "File Error");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NotAValidDateException.class)
    public String handleNotAValidDateException(NotAValidDateException ex, Model model) {
        model.addAttribute("type", "Invalid Date");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
