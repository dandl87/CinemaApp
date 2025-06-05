package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.MovieDTO;
import com.delorenzo.Cinema.dto.NewMovieDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.exception.StorageFileException;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.utils.MovieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;


@Service
public class MovieService {

    private final MoviesFromExcelService moviesFromExcelService;
    private final MovieRepository movieRepository;
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    public MovieService(MoviesFromExcelService moviesFromExcelService, MovieRepository movieRepository) {
        this.moviesFromExcelService = moviesFromExcelService;
        this.movieRepository = movieRepository;
    }

    public List<Movie> getMoviesFromExcel(String fileName) throws RuntimeException, IOException {
        logger.info("Getting movies from excel");
        return extractMoviesFromExcel(fileName);
    }
    public List<MovieDTO> findMovie(Movie movie) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnorePaths("duration")
                .withIgnorePaths("value")
                .withIgnorePaths("imax")
                .withMatcher("title", startsWith().ignoreCase())
                .withMatcher("director", startsWith().ignoreCase())
                .withMatcher("year", startsWith().ignoreCase());
        ;

        Example<Movie> example = Example.of(movie, matcher);
        List<Movie> movies = movieRepository.findAll(example);
        return MovieUtils.getMoviesDTOFromMovies(movies);
    }

    public List<MovieDTO> findMovieTitledLike(String title) {
        Movie movie = new Movie();
        movie.setTitle(title);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnorePaths("duration")
                .withIgnorePaths("value")
                .withIgnorePaths("imax")
                .withMatcher("title", startsWith().ignoreCase());
        Example<Movie> example = Example.of(movie, matcher);
        List<Movie> movies = movieRepository.findAll(example);
        return MovieUtils.getMoviesDTOFromMovies(movies);
    }

    public  Optional<MovieDTO> findMovieByTitle(String title) {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        return movie.map(MovieUtils::getMovieDTOFromMovie);
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }


    private Movie saveMovie(NewMovieDTO newMovie) {
        Movie movie = MovieUtils.createAMovie(newMovie);
        movieRepository.save(movie);
        logger.info("Movie {} created", movie.getTitle());
        return movie;
    }

    private List<Movie> extractMoviesFromExcel(String fileName) throws RuntimeException, IOException {
        List<NewMovieDTO> newMovies = null;
            newMovies = moviesFromExcelService.readFile(fileName);

        List<Movie> movies = new ArrayList<>();
        for (NewMovieDTO newMovie : newMovies) {
            Movie movie = saveMovie(newMovie);
            movies.add(movie);
        }
        return movies;
    }




}
