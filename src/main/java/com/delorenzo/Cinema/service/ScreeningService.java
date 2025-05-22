package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.ApplicationProperties;
import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.ScreeningRepository;
import com.delorenzo.Cinema.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScreeningService {

    private static final Logger logger = LoggerFactory.getLogger(ScreeningService.class);
    private final ScreeningRepository screeningRepository;
    private final Scheduler imaxScheduler;
    private final Scheduler regularScheduler;
    private final DateHolder currentDay;
    private final ApplicationProperties applicationProperties;
    private final Environment environment;


    public ScreeningService(ScreeningRepository screeningRepository, Scheduler imaxScheduler, Scheduler regularScheduler, DateHolder currentDay, ApplicationProperties applicationProperties, Environment environment) {
        this.screeningRepository = screeningRepository;
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
        this.currentDay = currentDay;
        this.applicationProperties = applicationProperties;
        this.environment = environment;
    }

    public void saveInitialScreenings(List<Screening> screeningsToBeSaved) {
        LocalDate today =  currentDay.getCurrentDate();
        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(today);
        for (Screening screening : screeningsToBeSaved) {
            screening.setNumberOfWeeks(1);
            screening.setFirstDay(lastMonday);
            screeningRepository.save(screening);
        }

    }

    public void saveScreenings(List<Screening> screeningsToBeSaved) {
        logger.info("saving screenings scheduled for next week");
        LocalDate nextMonday = Utils.findTheMondayOfTheWeek(currentDay.getCurrentDate().plusWeeks(1));
        for (Screening screening : screeningsToBeSaved) {
            if(screening.getFirstDay() == null)
                screening.setFirstDay(nextMonday);
            screeningRepository.save(screening);
        }

    }


    public List<Screening> getProgrammedScreenings() {
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

    public List<RoomScreeningDTO> getProgrammedScreeningsAsDTO() {
        List<Screening> screenings =  getProgrammedScreenings();
        return Utils.getRoomScreeningDTOList(screenings);
    }


    public List<RoomScreeningDTO> getListOfScreeningsOfTheWeek(LocalDate monday) {
        String weeksToLive = environment.getProperty("cinema.weeks-to-live");
        int weeksToliveAsInt = Integer.parseInt(weeksToLive);

        List<Screening> screeningsCreatedOnMonday  = screeningRepository.findScreeningByFirstDay(monday);

        List<Screening> screenings = new ArrayList<>(screeningsCreatedOnMonday);
        for(int i = 1; i<weeksToliveAsInt ; i++){
            for(int j = i +1 ; j<=weeksToliveAsInt ; j++){
                List<Screening> screeningsBefore = screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday.minusWeeks(i), j);
                screenings.addAll(screeningsBefore);
            }
        }
        return Utils.getRoomScreeningDTOList(screenings);
    }

    public List<Screening> getScreeningsOfMonday(LocalDate monday) {
        List<Screening> screenings = new ArrayList<>();
        int numberOfWeeks = applicationProperties.getWeeksToLive();
        for(int i = 0; i<numberOfWeeks; i++){
            List<Screening> screeningsTemp = screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday.minusWeeks(i), i+1);
            screenings.addAll(screeningsTemp);
        }
        return screenings;

    }



}
