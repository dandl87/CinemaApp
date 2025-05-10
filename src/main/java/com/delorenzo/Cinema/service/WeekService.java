package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.entity.Week;
import com.delorenzo.Cinema.logic.Scheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class WeekService {

    public  Week buildAWeek(List<Scheduler> schedulers, LocalDate monday) {
        Week week = new Week();
        Set<Screening> screenings = new HashSet<>();

        // for each scheduler filter each screening and add to the Set if is not null
        for (Scheduler scheduler : schedulers)
            scheduler.getScheduledScreenings().stream().filter(Objects::nonNull).forEach(s-> {
                screenings.add(s.clone());
                s.setNumberOfWeeks(s.getNumberOfWeeks() + 1);
                }
            );

        week.setScreenings(screenings);
        week.setMonday(monday);


        return week;
    }

}
