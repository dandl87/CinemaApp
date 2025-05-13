package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.NewMovie;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Week;
import com.delorenzo.Cinema.exception.DataRetrievingFromExcelException;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.repository.WeekRepository;
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
    private final WeekRepository weekRepository;
    private final MovieRepository movieRepository;
    private final Scheduler imaxScheduler;
    private final Scheduler regularScheduler;
    private final MovieService movieService;
    private final WeekService weekService;

    public MainService(
            MoviesFromExcelService moviesFromExcel,
            WeekRepository weekRepository, MovieRepository movieRepository,
            Scheduler imaxScheduler,
            Scheduler regularScheduler, MovieService movieService, WeekService weekService) {
        this.moviesFromExcel = moviesFromExcel;
        this.weekRepository = weekRepository;
        this.movieRepository = movieRepository;
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
        this.movieService = movieService;
        this.weekService = weekService;
    }

    //  first scheduling process with screenings/week saving ( from movies in db )
    public void initializationBatch() {

        List<Movie> regularMovies = movieRepository.findMovieByImax(false);
        List<Movie> imaxMovies = movieRepository.findMovieByImax(true);

        imaxScheduler.scheduling(imaxMovies);
        regularScheduler.scheduling(regularMovies);

        List<Scheduler> schedulers = new ArrayList<>();
        schedulers.add(imaxScheduler);
        schedulers.add(regularScheduler);

        LocalDate monday = LocalDate.of(2025, 3, 24);
        Week week = weekService.buildAWeek(schedulers, monday);
        weekRepository.save(week);
    }


    //  second scheduling process with screenings/week saving ( from movies in file excel )
    public void weeklyBatch(String fileName) throws DataRetrievingFromExcelException {

        logger.info("Weekly Batch process");
        try {
            List<NewMovie> newMovies = moviesFromExcel.readFile(fileName);
            List<Movie> regularMovies = new ArrayList<>();
            List<Movie> imaxMovies = new ArrayList<>();

            for (NewMovie newMovie : newMovies) {
                Movie movie = movieService.saveMovie(newMovie);
                if (movie.isImax())
                    imaxMovies.add(movie);
                else
                    regularMovies.add(movie);
            }

            imaxScheduler.scheduling(imaxMovies);
            regularScheduler.scheduling(regularMovies);
            List<Scheduler> schedulers = new ArrayList<>();
            schedulers.add(imaxScheduler);
            schedulers.add(regularScheduler);
            LocalDate monday = LocalDate.of(2025, 3, 31);
            Week week = weekService.buildAWeek(schedulers, monday);
            weekRepository.save(week);

        } catch (IOException e) {
            throw new DataRetrievingFromExcelException();
        }
    }

}
