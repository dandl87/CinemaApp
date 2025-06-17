package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SchedulingService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);
    private final Scheduler imaxScheduler;
    private final Scheduler regularScheduler;

    public SchedulingService(Scheduler imaxScheduler, Scheduler regularScheduler) {
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
    }

    public void scheduleNewMovies(List<Movie> movies) {
        logger.info("Scheduling new movies");
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

    public List<Screening> getScheduledScreenings() {
        List<Screening> screenings = new ArrayList<>();
        imaxScheduler.getScheduledScreenings().stream().filter(Objects::nonNull).forEach(s -> {
                    screenings.add(s.clone());
                }
        );
        regularScheduler.getScheduledScreenings().stream().filter(Objects::nonNull).forEach(s -> {
                    screenings.add(s.clone());
                }
        );
        return screenings;
    }

    public void removeScreenings() {
        imaxScheduler.removeScreenings();
        regularScheduler.removeScreenings();
    }

    public void incrementSchedulerMoviesNumberOfWeeks(){
        logger.info("Incrementing number Of weeks of schedulers movies");
        imaxScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));
        regularScheduler.getScheduledScreenings().stream()
                .filter(Objects::nonNull)
                .forEach(s -> s.setNumberOfWeeks(s.getNumberOfWeeks() + 1));
    }

}
