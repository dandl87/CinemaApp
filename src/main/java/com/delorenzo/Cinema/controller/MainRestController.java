package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.dto.MovieScreening;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.service.MovieService;
import com.delorenzo.Cinema.service.ScreeningService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/")
public class MainRestController {


    private final MovieService movieService;
    private final ScreeningService screeningService;

    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);

    public MainRestController(MovieService movieService, ScreeningService screeningService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
    }


    @GetMapping("/movie-screenings/week")
    public ResponseEntity<Set<MovieScreening>> findMovieScreeningsOfAWeek(@RequestParam String day) {
        logger.info("movies of {}", day);
        Set<MovieScreening> movieScreenings = screeningService.findMovieScreeningsOfTheWeek(day);
        return ResponseEntity.ok(movieScreenings);
    }

    @GetMapping("/movie-screenings/last-week")
    public ResponseEntity<Set<MovieScreening>> findMovieScreeningsOfTheLastWeek( ) {
        Set<MovieScreening> movieScreenings = screeningService.findMovieScreeningsOfTheLastWeek();
        return ResponseEntity.ok(movieScreenings);
    }



    // extra

    @PostMapping("/movies/find-by-example")
    public List<Movie> findMovie(
            @RequestBody @Valid Movie movie) {
        return movieService.findMovie(movie);
    }

    @GetMapping("/movies/find-by-title")
    public Optional<Movie> findMovie(
            @RequestParam String title) {
        return movieService.findMovieByTitle(title);
    }


    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovie(
            @RequestParam String title) {
        List<Movie> movies = movieService.findMovieTitledLike(title);
        return ResponseEntity.ok(movies);
    }




}
