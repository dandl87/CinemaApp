package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.ScreeningRepository;
import com.delorenzo.Cinema.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScreeningService {

    ScreeningRepository screeningRepository;
    Scheduler imaxScheduler;
    Scheduler regularScheduler;


    public ScreeningService(ScreeningRepository screeningRepository, Scheduler imaxScheduler, Scheduler regularScheduler) {
        this.screeningRepository = screeningRepository;
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
    }

    public void saveScreenings(List<Screening> screeningsToBeSaved) {
        LocalDate today = LocalDate.now();
        LocalDate lastMonday = Utils.findTheMondayOfTheWeek(today);
        for (Screening screening : screeningsToBeSaved) {
            screening.setFirstDay(lastMonday);
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

    public List<RoomScreeningDTO> findMovieScreeningsOfTheWeek(LocalDate monday) {

        List<Screening> screenings = getScreeningsOfMonday(monday);
        return Utils.getRoomScreeningDTOList(screenings);

    }

    private List<Screening> getScreeningsOfMonday(LocalDate monday) {
        List<Screening> screenings = new ArrayList<>();
        List<Screening> screeningsTemp = screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday, 1);
        List<Screening> screeningsTemp2 = screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday.minusDays(7), 2);
        screenings.addAll(screeningsTemp);
        screenings.addAll(screeningsTemp2);
        return screenings;

    }



}
