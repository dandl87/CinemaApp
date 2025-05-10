package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.DataSourceProperties;
import com.delorenzo.Cinema.exception.DataRetrievingFromExcelException;
import com.delorenzo.Cinema.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;


@EnableConfigurationProperties(DataSourceProperties.class)
@SpringBootApplication
public class CinemaApplication {

    private static final Logger logger = LoggerFactory.getLogger(CinemaApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(CinemaApplication.class);

        MainService mainService = context.getBean(MainService.class);

        mainService.initializationBatch();

        try {
            Thread.sleep(3000);
            mainService.weeklyBatch();
        } catch (DataRetrievingFromExcelException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }






}
