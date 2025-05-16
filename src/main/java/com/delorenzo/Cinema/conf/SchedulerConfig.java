package com.delorenzo.Cinema.conf;

import com.delorenzo.Cinema.logic.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SchedulerConfig {

    @Bean
    public Scheduler imaxScheduler( ) {
        return new Scheduler( "imax",2);
    }

    @Bean
    public Scheduler regularScheduler( ) {
        return new Scheduler( "regular",10);
    }


}
