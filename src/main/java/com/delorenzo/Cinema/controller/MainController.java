package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.service.MovieService;
import com.delorenzo.Cinema.service.ScreeningService;
import com.delorenzo.Cinema.service.StorageService;
import com.delorenzo.Cinema.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);
    private final MovieService movieService;
    private final StorageService storageService;
    private final ScreeningService screeningService;

    public MainController(MovieService movieService, StorageService storageService, ScreeningService screeningService) {
        this.movieService = movieService;
        this.storageService = storageService;
        this.screeningService = screeningService;
    }

    @GetMapping("/")
    public String home(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate monday = Utils.findTheMondayOfTheWeek(today);
        LocalDate nextMonday = Utils.findTheMondayOfTheWeek(monday.plusDays(7));

        List<RoomScreeningDTO> roomScreenings = screeningService.findMovieScreeningsOfTheWeek(monday);
        List<RoomScreeningDTO> roomScreeningsNextWeek = screeningService.getProgrammedScreeningsAsDTO();

        model.addAttribute("roomList", roomScreenings);
        model.addAttribute("nextWeek", "da " + nextMonday + " a " + nextMonday.plusDays(7));
        model.addAttribute("roomListNextWeek", roomScreeningsNextWeek);
        return "home";
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        List<Movie> filmList = movieService.findAllMovies();
        model.addAttribute("filmList", filmList);
        return "movieList";
    }

    @GetMapping("/movies/upload")
    public String upload(Model model) {
        return "upload";
    }

    @GetMapping("/files")
    public String getFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(Path::getFileName).collect(Collectors.toList()));
        return "uploadResultPage";
    }


}
