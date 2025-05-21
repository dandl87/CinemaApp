package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.dto.MovieDTO;
import com.delorenzo.Cinema.dto.MovieToSearchDTO;
import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.service.MovieService;
import com.delorenzo.Cinema.service.ScreeningService;
import com.delorenzo.Cinema.utils.Utils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "cinema api", description = "all my cinema endpoints")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/")
public class MainRestController {

    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final DateHolder currentDay;

    public MainRestController(MovieService movieService, ScreeningService screeningService, DateHolder currentDay) {
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.currentDay = currentDay;
    }

    @GetMapping("/movie-screenings/week")
    public ResponseEntity<List<RoomScreeningDTO>> findMovieScreeningsOfAWeek(@RequestParam LocalDate day) {
        LocalDate monday = Utils.findTheMondayOfTheWeek(day);
        List<RoomScreeningDTO> week = screeningService.getListOfScreeningsOfTheWeek(monday);
        return ResponseEntity.ok(week);
    }

    @GetMapping("/movie-screenings/last-week")
    public ResponseEntity<List<RoomScreeningDTO>> findMovieScreeningsOfTheLastWeek() {
        List<RoomScreeningDTO> week = screeningService.getListOfScreeningsOfTheWeek(Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate()));
        return ResponseEntity.ok(week);
    }

    @PostMapping("/movies/find-by-example")
    public ResponseEntity<List<MovieDTO>> findMovie(
            @RequestBody MovieToSearchDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getTitle(),movieDTO.getDirector(),movieDTO.getYear());
        List<MovieDTO> movies = movieService.findMovie(movie);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<MovieDTO>> searchMovie(
            @RequestParam String title) {
        List<MovieDTO> movies = movieService.findMovieTitledLike(title);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/find-by-title")
    public ResponseEntity<MovieDTO> findMovie(
            @RequestParam String title) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("error", "Movie Not Found");
        Optional<MovieDTO> movie = movieService.findMovieByTitle(title);
        return movie.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(new MovieDTO(), responseHeaders, 404));
    }

}
