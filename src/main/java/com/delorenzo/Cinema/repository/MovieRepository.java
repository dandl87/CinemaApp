package com.delorenzo.Cinema.repository;

import com.delorenzo.Cinema.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie,Long>, QueryByExampleExecutor<Movie> {
    Optional<Movie> findByTitle(String title);
    List<Movie> findMovieByImax(boolean imax);

}
