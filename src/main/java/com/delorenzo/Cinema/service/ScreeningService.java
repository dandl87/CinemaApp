package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.ApplicationProperties;
import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.ScreeningRepository;
import com.delorenzo.Cinema.utils.CalendarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScreeningService {

    private static final Logger logger = LoggerFactory.getLogger(ScreeningService.class);
    private final ScreeningRepository screeningRepository;
    private final Scheduler imaxScheduler;
    private final Scheduler regularScheduler;
    private final DateHolder calendar;
    private final ApplicationProperties applicationProperties;


    public ScreeningService(ScreeningRepository screeningRepository, Scheduler imaxScheduler, Scheduler regularScheduler, DateHolder calendar, ApplicationProperties applicationProperties) {
        this.screeningRepository = screeningRepository;
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
        this.calendar = calendar;
        this.applicationProperties = applicationProperties;
    }

    public void saveInitialScreenings(List<Screening> screeningsToBeSaved) {
        LocalDate today =  calendar.getCurrentDate();
        LocalDate lastMonday = CalendarUtils.findTheMondayOfTheWeek(today);
        for (Screening screening : screeningsToBeSaved) {
            screening.setNumberOfWeeks(1);
            screening.setFirstDay(lastMonday);
            screeningRepository.save(screening);
        }

    }

    public void saveScreenings(List<Screening> screeningsToBeSaved) {
        logger.info("saving screenings scheduled for next week");
        LocalDate nextMonday = CalendarUtils.findTheMondayOfTheWeek(calendar.getCurrentDate().plusWeeks(1));
        for (Screening screening : screeningsToBeSaved) {
            if(screening.getFirstDay() == null)
                screening.setFirstDay(nextMonday);
            screeningRepository.save(screening);
        }

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

    public List<Screening> getScreeningsOfAWeek(LocalDate monday) {
        int numberOfWeeks = applicationProperties.getWeeksToLive();
        List<Screening> screeningsCreatedOnMonday  = screeningRepository.findScreeningByFirstDay(monday);
        List<Screening> screenings = new ArrayList<>(screeningsCreatedOnMonday);
        for(int i = 1; i<numberOfWeeks ; i++){
            for(int j = i +1 ; j<=numberOfWeeks ; j++){
                List<Screening> screeningsBefore = screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday.minusWeeks(i), j);
                screenings.addAll(screeningsBefore);
            }
        }
        return screenings;
    }


}
