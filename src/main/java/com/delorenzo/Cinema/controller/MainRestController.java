package com.delorenzo.Cinema.controller;

import com.delorenzo.Cinema.dto.MovieDTO;
import com.delorenzo.Cinema.dto.MovieToSearchDTO;
import com.delorenzo.Cinema.dto.WeeklyScreeningsDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.service.MovieService;
import com.delorenzo.Cinema.service.ScreeningService;
import com.delorenzo.Cinema.utils.CalendarUtils;
import com.delorenzo.Cinema.utils.ScreeningUtils;
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

    public MainRestController(MovieService movieService, ScreeningService screeningService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    @GetMapping("/movie-screenings/week")
    public ResponseEntity<List<WeeklyScreeningsDTO>> findMovieScreeningsOfAWeek(@RequestParam LocalDate day) {
        LocalDate monday = CalendarUtils.findTheMondayOfTheWeek(day);
        List<WeeklyScreeningsDTO> screeningsAsDTO = getWeeklyScreenings(monday);
        return ResponseEntity.ok(screeningsAsDTO);
    }


    private List<WeeklyScreeningsDTO> getWeeklyScreenings(LocalDate monday){
        List<Screening> weeklyScreenings = screeningService.getScreeningsOfAWeek(monday);
        return ScreeningUtils.getRoomScreeningDTOList(weeklyScreenings);
    }

    @PostMapping("/movies/find-by-example")
    public ResponseEntity<List<MovieDTO>> findMovie(
            @RequestBody MovieToSearchDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getTitle(),movieDTO.getDirector(),movieDTO.getYear());
        List<MovieDTO> movies = getMovies(movie);
        return ResponseEntity.ok(movies);
    }

    private List<MovieDTO> getMovies(Movie movie){
        return movieService.findMovie(movie);
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<MovieDTO>> searchMovie(
            @RequestParam String title) {
        List<MovieDTO> movies = getMovies(title);
        return ResponseEntity.ok(movies);
    }

    private List<MovieDTO> getMovies(String title){
        return movieService.findMovieTitledLike(title);
    }

    @GetMapping("/movies/find-by-title")
    public ResponseEntity<MovieDTO> findMovie(
            @RequestParam String title) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("error", "Movie Not Found");
        Optional<MovieDTO> movie = getMovieByTitle(title);
        return movie.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(new MovieDTO(), responseHeaders, 404));
    }

    private Optional<MovieDTO> getMovieByTitle(String title){
        return movieService.findMovieByTitle(title);
    }



}
