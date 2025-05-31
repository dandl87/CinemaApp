package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
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
    private final SchedulingService schedulingService;

    private final DateHolder currentDay;

    public MainService(
            MovieService movieService,
            ScreeningService screeningService,
            SchedulingService schedulingService,
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
// the sunday process:
//    1. find the monday of the current week
//    2. increment number of Week of the movies in the schedulers
//    3. find all the screenings in the schedules
//    4. find all the screenings in the past week
//    5. Prepare the list of screenings for the next week
//          if present in last week increment number of weeks
//          if not add to the list
//    6. save to db
//    7. update schedulers and date

    public void sundayProcess() {
        logger.info("--- Sunday process started ---");
        long startTime = System.currentTimeMillis();
        incrementSchedulerMoviesNumberOfWeeks();
        prepareScreeningsForDb();
        saveScreenings(prepareScreeningsForDb());
        updateRuntime();
        logger.info("--- Sunday process ended in {} ms ---", System.currentTimeMillis() - startTime);
    }

    private void updateRuntime(){
        logger.info("updating current Date: {}", currentDay.getCurrentDate());
        logger.info("updating schedulers");
        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        schedulingService.removeScreenings();
        currentDay.updateDate(lastMonday.plusWeeks(1));
    }

    private void incrementSchedulerMoviesNumberOfWeeks() {
        schedulingService.incrementSchedulerMoviesNumberOfWeeks();
    }

    private List<Screening> prepareScreeningsForDb() {
        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        List<Screening> screeningsToBeSaved = screeningService.getProgrammedScreenings();
        List<Screening> screeningsOfTheWeek = screeningService.getScreeningsOfAWeek(lastMonday);
        return Utils.getScreeningsToBeSavedPreparedForDb(screeningsOfTheWeek, screeningsToBeSaved);
    }

    private void saveScreenings(List<Screening> screenings) {
        screeningService.saveScreenings(screenings);
    }

}
