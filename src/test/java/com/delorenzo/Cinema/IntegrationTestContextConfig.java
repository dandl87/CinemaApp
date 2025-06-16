package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.ApplicationProperties;
import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.exception.StorageException;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.ScreeningRepository;
import com.delorenzo.Cinema.service.FileSystemStorageService;
import com.delorenzo.Cinema.service.SchedulingService;
import com.delorenzo.Cinema.service.ScreeningService;
import com.delorenzo.Cinema.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationTestContextConfig {

    private DateHolder currentDay;
    private Scheduler imaxScheduler;
    private Scheduler regularScheduler;
    private ApplicationProperties applicationProperties;

    @Autowired
    ScreeningRepository screeningRepository;

    @Bean
    public ApplicationProperties applicationProperties() {
        this.applicationProperties = new ApplicationProperties();
        return applicationProperties;
    }

    @Bean
    public DateHolder currentDay( ) {
        this.currentDay = new DateHolder();
        return currentDay;
    }


    @Bean
    public StorageService storageService( ) throws StorageException {
        return new FileSystemStorageService(new StorageProperties());
    }

    @Bean
    public Scheduler imaxScheduler( ) {

        this.imaxScheduler = new Scheduler("imaxScheduler",2);
        return imaxScheduler;
    }
    @Bean
    public Scheduler regularScheduler( ) {

        this.regularScheduler = new Scheduler("regularScheduler",10);
        return regularScheduler;

    }


    @Bean
    public SchedulingService schedulingService(Scheduler imaxScheduler, Scheduler regularScheduler) {

        return new SchedulingService(imaxScheduler, regularScheduler);
    }
    @Bean
    public ScreeningService screeningService( ) {

        return new ScreeningService(screeningRepository, imaxScheduler, regularScheduler, currentDay, applicationProperties);
    }



}
