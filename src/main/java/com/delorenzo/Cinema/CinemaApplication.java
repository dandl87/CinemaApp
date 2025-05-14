package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.service.MainService;
import com.delorenzo.Cinema.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;


@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class CinemaApplication {

    private static final Logger logger = LoggerFactory.getLogger(CinemaApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(CinemaApplication.class);

        MainService mainService = context.getBean(MainService.class);

        StorageService storageService = context.getBean(StorageService.class);
        storageService.deleteAll();
        storageService.init();

        mainService.initializationBatch();


    }





}
