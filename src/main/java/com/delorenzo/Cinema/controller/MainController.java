package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.service.MovieService;
import com.delorenzo.Cinema.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);
    private final MovieService movieService;
    private final StorageService storageService;

    public MainController(MovieService movieService, StorageService storageService) {
        this.movieService = movieService;
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("titoloPagina", "Home Page");
        return "home";
    }

    @GetMapping("/movies")
    public String getMovies(Model model){
        List<Movie> filmList = movieService.findAllMovies();
        model.addAttribute("filmList", filmList);
        return "movieList";
    }

    @GetMapping("/movies/upload")
    public String upload(Model model){
        return "upload";
    }

    @GetMapping("/files")
    public String getFiles(Model model) throws IOException {

        model.addAttribute("files",storageService.loadAll().map(Path::getFileName).collect(Collectors.toList()));

        return "uploadResultPage";
    }


    @GetMapping("/screenings")
    public String getScreenings(Model model){
        return "screenings";
    }
}
