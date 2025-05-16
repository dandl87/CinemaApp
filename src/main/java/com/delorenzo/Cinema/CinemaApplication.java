package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.service.MainService;
import com.delorenzo.Cinema.service.SchedulingService;
import com.delorenzo.Cinema.service.ScreeningService;
import com.delorenzo.Cinema.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;


@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class CinemaApplication {

    private static final Logger logger = LoggerFactory.getLogger(CinemaApplication.class);

    public static void main(String[] args) {

        // starting operations
        ConfigurableApplicationContext context = SpringApplication.run(CinemaApplication.class);

        SchedulingService schedulingService = context.getBean(SchedulingService.class);
        ScreeningService screeningService = context.getBean(ScreeningService.class);
        MovieRepository movieRepository = context.getBean(MovieRepository.class);
        Scheduler imaxScheduler = context.getBean("imax", Scheduler.class);
        Scheduler regularScheduler = context.getBean("regular", Scheduler.class);
        StorageService storageService = context.getBean(StorageService.class);

        storageService.deleteAll();
        storageService.init();

        List<Movie> regularMovies = movieRepository.findMovieByImax(false);
        List<Movie> imaxMovies = movieRepository.findMovieByImax(true);
        imaxScheduler.scheduling(imaxMovies);
        regularScheduler.scheduling(regularMovies);

        List<Screening> screeningsToBeSaved = screeningService.getProgrammedScreenings();
        schedulingService.incrementSchedulerMoviesNumberOfWeeks();
        screeningService.saveInitialScreenings(screeningsToBeSaved);


    }





}
