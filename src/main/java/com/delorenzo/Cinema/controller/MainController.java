package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.dto.MovieDTO;
import com.delorenzo.Cinema.dto.WeeklyScreeningsDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.exception.NotAValidDateException;
import com.delorenzo.Cinema.exception.StorageException;
import com.delorenzo.Cinema.service.*;
import com.delorenzo.Cinema.utils.CalendarUtils;
import com.delorenzo.Cinema.utils.MovieUtils;
import com.delorenzo.Cinema.utils.ScreeningUtils;
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
    private final MainService mainService;
    private final MovieService movieService;
    private final StorageService storageService;
    private final ScreeningService screeningService;
    private final DateHolder calendar;

    public MainController(MovieService movieService,
                          StorageService storageService,
                          ScreeningService screeningService,
                          MainService mainService,
                          DateHolder calendar) {
        this.movieService = movieService;
        this.storageService = storageService;
        this.screeningService = screeningService;
        this.mainService = mainService;
        this.calendar = calendar;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        LocalDate monday = CalendarUtils.findTheMondayOfTheWeek(calendar.getCurrentDate());
        LocalDate nextMonday = monday.plusDays(7);
        List<WeeklyScreeningsDTO> currentWeekScreeningsAsDTO = getWeeklyScreenings(monday);
        List<WeeklyScreeningsDTO> nextWeekExpectedScreeningsAsDTO = getNextWeekExpectedScreenings();
        model.addAttribute("screeningList", currentWeekScreeningsAsDTO);
        model.addAttribute("nextWeek", "da " + nextMonday + " a " + nextMonday.plusDays(7));
        model.addAttribute("roomListNextWeek", nextWeekExpectedScreeningsAsDTO);
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "home";
    }

    @GetMapping("/screenings")
    public String showWeeklyScreenings(@RequestParam(value = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day, Model model){
        LocalDate monday = assignMonday(day);
        String mondayFormatted = mondayFormatter();;
        List<WeeklyScreeningsDTO> weekScreeningsAsDTO = getWeeklyScreenings(monday);
        model.addAttribute("screeningList", weekScreeningsAsDTO);
        model.addAttribute("dayFormatted", mondayFormatted);
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "screeningsPage";
    }

    private String mondayFormatter(){
        LocalDate monday = setMondayOfThisWeek();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return monday.format(formatter);
    }
    private LocalDate assignMonday(LocalDate day){
        LocalDate monday;
        if (day==null)
            monday = setMondayOfThisWeek();
        else {
            monday = CalendarUtils.findTheMondayOfTheWeek(day);
            if (monday.isAfter(calendar.getCurrentDate()))
                throw new NotAValidDateException("Future Screenings are not defined");
        }
        return monday;
    }

    private List<WeeklyScreeningsDTO> getWeeklyScreenings(LocalDate monday){
        List<Screening> weeklyScreenings = screeningService.getScreeningsOfAWeek(monday);
        return ScreeningUtils.getRoomScreeningDTOList(weeklyScreenings);
    }

    private List<WeeklyScreeningsDTO> getNextWeekExpectedScreenings(){
        List<Screening> weeklyScreenings = screeningService.getScheduledScreenings();
        return ScreeningUtils.getRoomScreeningDTOList(weeklyScreenings);
    }

    @GetMapping("/movies")
    public String showMovieList(Model model) {
        List<MovieDTO> moviesAsDTO = getMovies();
        model.addAttribute("filmList", moviesAsDTO);
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "movieList";
    }

    private List<MovieDTO> getMovies() {
        List<Movie> movies = movieService.findAllMovies();
        return MovieUtils.getMoviesDTOFromMovies(movies);
    }


    @GetMapping("/upload")
    public String showUploadPage(Model model) {
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "upload";
    }

    @GetMapping("/files")
    public String showUploadFiles(Model model) throws StorageException {
        List<Path> filePaths = getFilesLoadedPath();
        model.addAttribute("files", filePaths);
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "uploadResultPage";
    }

    private List<Path> getFilesLoadedPath() throws StorageException {
        return storageService.loadAll().map(Path::getFileName).collect(Collectors.toList());
    }

    @GetMapping("/sunday")
    public String triggerSundayProcessing() {
        mainService.sundayProcess();
        return "redirect:/";
    }

    private LocalDate setMondayOfThisWeek(){
       return CalendarUtils.findTheMondayOfTheWeek(calendar.getCurrentDate());
    }




}
