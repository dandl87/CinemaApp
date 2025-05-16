package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.logic.Scheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SchedulingService {
    private final Scheduler imaxScheduler;
    private final Scheduler regularScheduler;

    public SchedulingService(Scheduler imaxScheduler, Scheduler regularScheduler) {
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
    }

    public void incrementSchedulerMoviesNumberOfWeeks(){
        imaxScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));
        regularScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));
    }

    public void scheduleNewMovies(List<Movie> movies) {
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

    public void removeScreenings() {
        imaxScheduler.removeScreenings();
        regularScheduler.removeScreenings();
    }

}
