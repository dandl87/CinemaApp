package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.NewMovieDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    private Movie createAMovie(NewMovieDTO newMovie) {
        Movie movie = new Movie();
        movie.setTitle(newMovie.getTitle());
        movie.setDirector(newMovie.getDirector());
        movie.setYear(String.valueOf(newMovie.getYear()));
        movie.setDuration(newMovie.getDuration());
        movie.setImax(newMovie.isImax());
        movie.setValue(newMovie.getValue());
        return movie;
    }

    public Movie saveMovie(NewMovieDTO newMovie) {
        Movie movie = createAMovie(newMovie);
        movieRepository.save(movie);
        logger.info("Movie {} created", movie.getTitle());
        return movie;
    }

    public List<Movie> findMovie(Movie movie) {
        Example<Movie> example = Example.of(movie);
        return movieRepository.findAll(example);
    }

    public List<Movie> findMovieTitledLike(String title) {
        Movie movie = new Movie();
        movie.setTitle(title);
        ExampleMatcher matcher = matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues()
                .withMatcher("title", match -> match.contains());
        Example<Movie> example = Example.of(movie, matcher);
        return movieRepository.findAll(example);
    }

    public Optional<Movie> findMovieByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

}
