package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.exception.NotAValidDateException;
import com.delorenzo.Cinema.service.*;
import com.delorenzo.Cinema.utils.DateUtils;
import com.delorenzo.Cinema.utils.ScreeningUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        LocalDate monday = DateUtils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        LocalDate nextMonday = DateUtils.findTheMondayOfTheWeek(monday.plusDays(7));

        List<Screening> screeningsTemp = screeningService.getScreeningsOfAWeek(monday);
        List<RoomScreeningDTO> screenings = ScreeningUtils.getRoomScreeningDTOList(screeningsTemp);
        List<Screening> screeningsNextWeekTemp = screeningService.getProgrammedScreenings();
        List<RoomScreeningDTO> screeningsNextWeek = ScreeningUtils.getRoomScreeningDTOList(screeningsNextWeekTemp);

        model.addAttribute("screeningList", screenings);
        model.addAttribute("nextWeek", "da " + nextMonday + " a " + nextMonday.plusDays(7));
        model.addAttribute("roomListNextWeek", screeningsNextWeek);
        model.addAttribute("currentDay", currentDay.getCurrentDate());
        return "home";
    }
    @GetMapping("/screenings")
    public String getScreenings(@RequestParam(value = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day, Model model){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dayFormatted;
        LocalDate monday;
        if (day==null)
            monday = DateUtils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        else {
            monday = DateUtils.findTheMondayOfTheWeek(day);
            if (monday.isAfter(currentDay.getCurrentDate()))
                throw new NotAValidDateException("Future Screenings are not defined");
        }
        dayFormatted = monday.format(formatter);
        List<Screening> screeningsTemp = screeningService.getScreeningsOfAWeek(monday);
        List<RoomScreeningDTO> screenings = ScreeningUtils.getRoomScreeningDTOList(screeningsTemp);
        model.addAttribute("screeningList", screenings);
        model.addAttribute("dayFormatted", dayFormatted);
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
    public String sundayProcess() {
        mainService.sundayProcess();
        return "redirect:/";
    }


}
