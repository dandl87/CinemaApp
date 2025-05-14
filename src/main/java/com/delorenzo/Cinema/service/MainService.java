package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.NewMovieDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.exception.DataRetrievingFromExcelException;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.MovieRepository;
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

    public MainService(
            MoviesFromExcelService moviesFromExcel,
            MovieRepository movieRepository,
            Scheduler imaxScheduler,
            Scheduler regularScheduler, MovieService movieService, ScreeningService screeningService) {
        this.moviesFromExcel = moviesFromExcel;
        this.movieRepository = movieRepository;
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    //  first scheduling process with screenings/week saving ( from movies in db )
    public void initializationBatch() {

        List<Movie> regularMovies = movieRepository.findMovieByImax(false);
        List<Movie> imaxMovies = movieRepository.findMovieByImax(true);

        imaxScheduler.scheduling(imaxMovies);
        regularScheduler.scheduling(regularMovies);


        LocalDate monday = LocalDate.of(2025, 3, 24);

        // Salvare le proiezioni prese dal db
        screeningService.defineScreeningsFirstTime(monday);


    }


    // To Be Done
    public void weeklyBatch(String fileName) throws DataRetrievingFromExcelException {

        logger.info("Weekly Batch process");
        try {
            List<NewMovieDTO> newMovies = moviesFromExcel.readFile(fileName);
            List<Movie> regularMovies = new ArrayList<>();
            List<Movie> imaxMovies = new ArrayList<>();

            for (NewMovieDTO newMovie : newMovies) {
                Movie movie = movieService.saveMovie(newMovie);
                if (movie.isImax())
                    imaxMovies.add(movie);
                else
                    regularMovies.add(movie);
            }

            imaxScheduler.scheduling(imaxMovies);
            regularScheduler.scheduling(regularMovies);


            LocalDate today = LocalDate.of(2025, 3, 29);
            LocalDate monday = LocalDate.of(2025, 3, 31);


            // Salvare le proiezioni
            screeningService.defineScreenings(today,monday);




        } catch (IOException e) {
            throw new DataRetrievingFromExcelException();
        }
    }

}
