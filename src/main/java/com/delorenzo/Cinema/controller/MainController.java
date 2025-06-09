package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.dto.MovieDTO;
import com.delorenzo.Cinema.dto.WeeklyScreeningsDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.exception.NotAValidDateException;
import com.delorenzo.Cinema.exception.StorageException;
import com.delorenzo.Cinema.exception.UploadFileException;
import com.delorenzo.Cinema.service.*;
import com.delorenzo.Cinema.utils.CalendarUtils;
import com.delorenzo.Cinema.utils.MovieUtils;
import com.delorenzo.Cinema.utils.ScreeningUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    public MainController(MovieService movieService, StorageService storageService, ScreeningService screeningService, MainService mainService, DateHolder calendar) {
        this.movieService = movieService;
        this.storageService = storageService;
        this.screeningService = screeningService;
        this.mainService = mainService;
        this.calendar = calendar;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        LocalDate monday = CalendarUtils.findTheMondayOfTheWeek(calendar.getCurrentDate());
        LocalDate nextMonday = CalendarUtils.findTheMondayOfTheWeek(monday.plusDays(7));
        List<Screening> weeklyScreenings = screeningService.getScreeningsOfAWeek(monday);
        List<WeeklyScreeningsDTO> weeklyScreeningsDTOS = ScreeningUtils.getRoomScreeningDTOList(weeklyScreenings);
        List<Screening> screeningsNextWeekTemp = screeningService.getScheduledScreenings();
        List<WeeklyScreeningsDTO> screeningsNextWeek = ScreeningUtils.getRoomScreeningDTOList(screeningsNextWeekTemp);
        model.addAttribute("screeningList", weeklyScreeningsDTOS);
        model.addAttribute("nextWeek", "da " + nextMonday + " a " + nextMonday.plusDays(7));
        model.addAttribute("roomListNextWeek", screeningsNextWeek);
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "home";
    }
    @GetMapping("/screenings")
    public String showWeeklyScreenings(@RequestParam(value = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day, Model model){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dayFormatted;
        LocalDate monday;
        if (day==null)
            monday = CalendarUtils.findTheMondayOfTheWeek(calendar.getCurrentDate());
        else {
            monday = CalendarUtils.findTheMondayOfTheWeek(day);
            if (monday.isAfter(calendar.getCurrentDate()))
                throw new NotAValidDateException("Future Screenings are not defined");
        }
        dayFormatted = monday.format(formatter);
        List<Screening> weeklyScreenings = screeningService.getScreeningsOfAWeek(monday);
        List<WeeklyScreeningsDTO> weeklyScreeningsDTOS = ScreeningUtils.getRoomScreeningDTOList(weeklyScreenings);
        model.addAttribute("screeningList", weeklyScreeningsDTOS);
        model.addAttribute("dayFormatted", dayFormatted);
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "screeningsPage";
    }

    @GetMapping("/movies")
    public String showMovieList(Model model) {
        List<Movie> movies = movieService.findAllMovies();
        // devo usare i dtos
        List<MovieDTO> moviesDTOS = MovieUtils.getMoviesDTOFromMovies(movies);
        model.addAttribute("filmList", moviesDTOS);
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "movieList";
    }

    @GetMapping("/movies/upload")
    public String showUploadPage(Model model) {
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "upload";
    }

    @GetMapping("/files")
    public String showUploadFiles(Model model) {
        model.addAttribute("files", storageService.loadAll().map(Path::getFileName).collect(Collectors.toList()));
        model.addAttribute("currentDay", calendar.getCurrentDate());
        return "uploadResultPage";
    }

    @GetMapping("/sunday")
    public String triggerSundayProcessing() {
        mainService.sundayProcess();
        return "redirect:/";
    }

    @ExceptionHandler(NotAValidDateException.class)
    public String handleNotAValidDateException(NotAValidDateException ex, Model model) {
            model.addAttribute("type", "Invalid Date");
            model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(StorageException.class)
    public String handleStorageFileError(Exception ex, Model model){
        model.addAttribute("type", "File Error");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UploadFileException.class)
    public String handleUploadFileError(Exception ex, Model model){
        model.addAttribute("type", "File Error");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }



}
