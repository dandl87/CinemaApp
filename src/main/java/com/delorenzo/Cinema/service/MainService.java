package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class MainService {

    private static final Logger logger = LoggerFactory.getLogger(MainService.class);
    private final MovieService movieService;
    private final ScreeningService screeningService;
    private SchedulingService schedulingService;

    private final DateHolder currentDay;

    public MainService(
            MovieRepository movieRepository,
            MovieService movieService,
            ScreeningService screeningService, SchedulingService schedulingService,
            DateHolder currentDay) {
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.schedulingService = schedulingService;
        this.currentDay = currentDay;
    }


    public void batch(String fileName) {
        logger.info("--- Batch process started ---");
        List<Movie> movies;
        try {
            movies = movieService.getMoviesFromExcel(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        schedulingService.scheduleNewMovies(movies);
        logger.info("--- Batch process ended ---");
    }

    public void sunday() {
        logger.info("--- Sunday process started ---");
        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        schedulingService.incrementSchedulerMoviesNumberOfWeeks();
        List<Screening> screeningsToBeSaved = screeningService.getProgrammedScreenings();
        List<Screening> screeningsOfTheWeek = screeningService.getScreeningsOfMonday(lastMonday);
        List<Screening> screeningsToBeSavedPreparedForDb = Utils.getScreeningsToBeSavedPreparedForDb(screeningsOfTheWeek, screeningsToBeSaved);
        screeningService.saveScreenings(screeningsToBeSavedPreparedForDb);
        updateRuntime();
        logger.info("--- Sunday process ended ---");
    }

    private void updateRuntime(){
        logger.info("updating current Date: {}", currentDay.getCurrentDate());
        logger.info("updating schedulers");
        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        schedulingService.removeScreenings();
        currentDay.updateDate(lastMonday.plusWeeks(1));
    }

}
