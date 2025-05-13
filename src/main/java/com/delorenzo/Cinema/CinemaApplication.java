package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.service.MainService;
import com.delorenzo.Cinema.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class CinemaApplication {

    private static final Logger logger = LoggerFactory.getLogger(CinemaApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(CinemaApplication.class);

        MainService mainService = context.getBean(MainService.class);

        mainService.initializationBatch();


    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }




}
