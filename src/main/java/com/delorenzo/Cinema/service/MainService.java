package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.dto.NewMovieDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
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
    private final MoviesFromExcelService moviesFromExcel;
    private final MovieRepository movieRepository;
    private final Scheduler imaxScheduler;
    private final Scheduler regularScheduler;
    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final DateHolder currentDay;

    public MainService(
            MoviesFromExcelService moviesFromExcel,
            MovieRepository movieRepository,
            Scheduler imaxScheduler,
            Scheduler regularScheduler,
            MovieService movieService,
            ScreeningService screeningService,
            DateHolder currentDay) {
        this.moviesFromExcel = moviesFromExcel;
        this.movieRepository = movieRepository;
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.currentDay = currentDay;
    }

    public void initializationBatch() {
        List<Movie> regularMovies = movieRepository.findMovieByImax(false);
        List<Movie> imaxMovies = movieRepository.findMovieByImax(true);
        imaxScheduler.scheduling(imaxMovies);
        regularScheduler.scheduling(regularMovies);
    }
    public void initializationSunday() {
        List<Screening> screeningsToBeSaved = screeningService.getProgrammedScreenings();
        imaxScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));
        regularScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));
        screeningService.saveInitialScreenings(screeningsToBeSaved);
    }

    public void batch(String fileName) {
        logger.info("Batch process started");
        List<Movie> movies;
        try {
            movies = saveMovies(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        schedule(movies);
        logger.info("Batch process ended");
    }

    public void sunday() {
        logger.info("Sunday process started");

        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());

        incrementNumberOfWeeks();
        List<Screening> screeningsToBeSaved = screeningService.getProgrammedScreenings();
        List<Screening> screeningsOfTheWeek = screeningService.getScreeningsOfMonday(lastMonday);
        List<Screening> screeningsToBeSavedPreparedForDb = getScreeningsToBeSavedPreparedForDb(screeningsOfTheWeek, screeningsToBeSaved);
        screeningService.saveScreenings(screeningsToBeSavedPreparedForDb);
        updateRuntime();
        logger.info("Sunday process started");

    }

    private List<Movie> saveMovies(String fileName) throws IOException {
        List<NewMovieDTO> newMovies = moviesFromExcel.readFile(fileName);
        List<Movie> movies = new ArrayList<>();
        for (NewMovieDTO newMovie : newMovies) {
            Movie movie = movieService.saveMovie(newMovie);
            movies.add(movie);
        }
        return movies;

    }
    private void schedule(List<Movie> movies) {
        List<Movie> imaxMovies = new ArrayList<>();
        List<Movie> regularMovies = new ArrayList<>();

        for (Movie movie : movies) {
            if(movie.isImax())
                imaxMovies.add(movie);
            else
                regularMovies.add(movie);
        }

        imaxScheduler.scheduling(imaxMovies);
        regularScheduler.scheduling(regularMovies);
    }

    private void incrementNumberOfWeeks(){
        imaxScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));
        regularScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));

    }

    private List<Screening> getScreeningsToBeSavedPreparedForDb(List<Screening> screeningsOfTheWeek,List<Screening> screeningsToBeSaved) {
        List<Screening> screeningsToBeSavedPreparedForDb = new ArrayList<>();

        screeningsToBeSaved.forEach(screening ->
                {
                    Optional<Screening> screeningOpt = Utils.extractScreeningFromAList(screening, screeningsOfTheWeek);
                    if (screeningOpt.isPresent()) {
                        screeningOpt.get().setNumberOfWeeks(screeningOpt.get().getNumberOfWeeks() + 1);
                        screeningsToBeSavedPreparedForDb.add(screeningOpt.get());
                    } else
                        screeningsToBeSavedPreparedForDb.add(screening);
                }
        );
    return screeningsToBeSavedPreparedForDb;
    }

    private void updateRuntime(){
        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate());
        imaxScheduler.removeScreenings();
        regularScheduler.removeScreenings();
        currentDay.updateDate(lastMonday.plusWeeks(1));
    }

}
