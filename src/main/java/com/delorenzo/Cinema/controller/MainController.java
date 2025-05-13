package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);
    private final MovieService movieService;

    public MainController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String home(Model model){
        model.addAttribute("titoloPagina", "Home Page");
        return "home";
    }

    @GetMapping("/movies")
    public String getMovies(Model model){
        List<Movie> filmList = movieService.findAllMovies();
        model.addAttribute("filmList", filmList);
        return "movies";
    }

    @GetMapping("/movies/upload")
    public String upload(Model model){
        return "upload";
    }

    @GetMapping("/movies/insert")
    public String insertMovies(Model model){
        boolean insertionResult = false;
        model.addAttribute("insertion",insertionResult);
        return "upload";
    }

    @GetMapping("/screenings")
    public String getScreenings(Model model){
        return "screenings";
    }
}
