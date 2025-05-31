package com.delorenzo.Cinema.utils;

import com.delorenzo.Cinema.dto.MovieDTO;
import com.delorenzo.Cinema.dto.NewMovieDTO;
import com.delorenzo.Cinema.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieUtils {
    public static List<MovieDTO> getMoviesDTOFromMovies(List<Movie> movies){
        List<MovieDTO> movieDTOS = new ArrayList<>();
        for(Movie movie : movies){
            MovieDTO movieDTO = getMovieDTOFromMovie(movie);
            movieDTOS.add(movieDTO);
        }

        return movieDTOS;
    }
    public static MovieDTO getMovieDTOFromMovie(Movie movie){
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDuration(movie.getDuration());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setValue(movie.getValue());
        movieDTO.setYear(movie.getYear());
        movieDTO.setScreeningsDates(movie.getScreenings());
        return movieDTO;
    }

    public static Movie createAMovie(NewMovieDTO newMovie) {
        Movie movie = new Movie();
        movie.setTitle(newMovie.getTitle());
        movie.setDirector(newMovie.getDirector());
        movie.setYear(String.valueOf(newMovie.getYear()));
        movie.setDuration(newMovie.getDuration());
        movie.setImax(newMovie.isImax());
        movie.setValue(newMovie.getValue());
        return movie;
    }
}
