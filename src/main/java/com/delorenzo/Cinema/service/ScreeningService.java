package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.MovieScreening;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.entity.Week;
import com.delorenzo.Cinema.repository.WeekRepository;
import com.delorenzo.Cinema.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ScreeningService {

    private final WeekRepository weekRepository;

    public ScreeningService(WeekRepository weekRepository) {
        this.weekRepository = weekRepository;
    }


    public Set<MovieScreening> findMovieScreeningsOfTheWeek(String day) {
        final LocalDate daySearched = LocalDate.parse(day);
        LocalDate mondayFounded = Utils.findTheMondayOfTheWeek(daySearched);
        Optional<Week> week = weekRepository.findByMonday(mondayFounded);
        if (week.isPresent()) {
            Set<Screening> screenings = week.get().getScreenings();
            Optional<Set<MovieScreening>> movieScreenings = Utils.createMovieScreeningsFromScreenings(screenings);
            return movieScreenings.orElse(new HashSet<>());
        }
        return new HashSet<>();
    }


    public Set<MovieScreening> findMovieScreeningsOfTheLastWeek() {
        Week week = weekRepository.findFirstByOrderByMondayDesc();
        Set<Screening> screenings = week.getScreenings();
        Optional<Set<MovieScreening>> movieScreenings = Utils.createMovieScreeningsFromScreenings(screenings);
        return movieScreenings.orElse(new HashSet<>());

    }
}
