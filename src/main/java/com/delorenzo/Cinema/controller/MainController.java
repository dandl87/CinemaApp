package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.service.*;
import com.delorenzo.Cinema.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);
    private final MovieService movieService;
    private final StorageService storageService;
    private final ScreeningService screeningService;
    private final MainService mainService;
    private final DateHolder currentDay;

    public MainController(MovieService movieService, StorageService storageService, ScreeningService screeningService, MainService mainService, DateHolder currentDay) {
        this.movieService = movieService;
        this.storageService = storageService;
        this.screeningService = screeningService;
        this.mainService = mainService;
        this.currentDay = currentDay;
    }

    @GetMapping("/")
    public String home(Model model) {
        LocalDate monday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        LocalDate nextMonday = Utils.findTheMondayOfTheWeek(monday.plusDays(7));

        List<RoomScreeningDTO> screenings = screeningService.getListOfScreeningsOfTheWeek(monday);
        List<RoomScreeningDTO> screeningsNextWeek = screeningService.getProgrammedScreeningsAsDTO();

        model.addAttribute("screeningList", screenings);
        model.addAttribute("nextWeek", "da " + nextMonday + " a " + nextMonday.plusDays(7));
        model.addAttribute("roomListNextWeek", screeningsNextWeek);
        model.addAttribute("currentDay", currentDay.getCurrentDate());
        return "home";
    }
    @GetMapping("/screenings")
    public String getScreenings(@RequestParam(value = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day, Model model){
        LocalDate today = currentDay.getCurrentDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayFormatted = today.format(formatter);
        LocalDate monday;
        if (day==null)
           monday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        else
            monday = Utils.findTheMondayOfTheWeek(day);
        List<RoomScreeningDTO> screenings = screeningService.getListOfScreeningsOfTheWeek(monday);
        model.addAttribute("screeningList", screenings);
        model.addAttribute("currentDayFormatted", todayFormatted);
        model.addAttribute("currentDay", currentDay.getCurrentDate());
        return "screeningsPage";
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        List<Movie> filmList = movieService.findAllMovies();
        model.addAttribute("filmList", filmList);
        model.addAttribute("currentDay", currentDay.getCurrentDate());
        return "movieList";
    }

    @GetMapping("/movies/upload")
    public String upload(Model model) {
        model.addAttribute("currentDay", currentDay.getCurrentDate());
        return "upload";
    }

    @GetMapping("/files")
    public String getFiles(Model model) {
        model.addAttribute("files", storageService.loadAll().map(Path::getFileName).collect(Collectors.toList()));
        model.addAttribute("currentDay", currentDay.getCurrentDate());
        return "uploadResultPage";
    }

    @GetMapping("/sunday")
    public String sunday() {
        mainService.sunday();
        return "redirect:/";
    }


}
