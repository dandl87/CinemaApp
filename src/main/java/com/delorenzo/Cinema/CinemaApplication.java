package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.service.SchedulingService;
import com.delorenzo.Cinema.service.ScreeningService;
import com.delorenzo.Cinema.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.List;


@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class CinemaApplication {
    private static final Logger logger = LoggerFactory.getLogger(CinemaApplication.class);

    public static void main(String[] args) {
        // starting operations
        SpringApplication application = new SpringApplication(CinemaApplication.class);
        application.setAdditionalProfiles("dev");
        application.setBannerMode(Banner.Mode.OFF);
        application.addListeners((ApplicationListener<ApplicationStartedEvent>) event -> System.out.println("Applicazione Avviata!"));
        application.run(args);
    }

    @Bean
    @Order(2)
    CommandLineRunner init(StorageService storageService,
                           SchedulingService schedulingService,
                           ScreeningService screeningService,
                           MovieRepository movieRepository
    ) {
        return args -> {
            logger.info("--- Initialization phase started ---");
            storageService.deleteAll();
            storageService.init();
            List<Movie> movies = movieRepository.findAll();
            schedulingService.scheduleNewMovies(movies);
            List<Screening> screeningsToBeSaved = schedulingService.getScheduledScreenings();
            schedulingService.incrementSchedulerMoviesNumberOfWeeks();
            screeningService.saveInitialScreenings(screeningsToBeSaved);
            logger.info("--- Initialization phase ended ---");
        };
    }


}
