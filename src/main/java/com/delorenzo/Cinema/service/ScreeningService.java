package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.MovieScreeningDTO;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.ScreeningRepository;
import com.delorenzo.Cinema.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void defineScreeningsFirstTime(LocalDate monday){

        List<Screening> screeningsToBeSaved = Utils.buildAWeek(imaxScheduler,regularScheduler, null, monday);

        for(Screening screening : screeningsToBeSaved) {
            screening.setFirstDay(monday);
            screeningRepository.save(screening);
        }


    }

    // To Be Done
    public void defineScreenings(LocalDate today, LocalDate monday){

        List<Screening> screeningsToBeSaved = Utils.buildAWeek(imaxScheduler,regularScheduler, today, monday);
        //  Date le proiezioni del prossimo monday
        //  recupero quelle della settimana precedente
        // aggiungo una settimana se già esistono
        // salvo le nuove
        // il problema dell'inserimento di più film
        // prima del monday va risolto


        List<Screening> screenings = getScreeningsOfMonday(monday);


        for(Screening screening : screeningsToBeSaved) {
                screening.setFirstDay(monday);
                screeningRepository.save(screening);
            }


    }
    public List<Screening> getScreenings(LocalDate monday, int numberOfWeeks) {
        return screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday, numberOfWeeks);
    }



    public Set<MovieScreeningDTO> findMovieScreeningsOfTheWeek(String day) {
        final LocalDate daySearched = LocalDate.parse(day);
        LocalDate mondayFounded = Utils.findTheMondayOfTheWeek(daySearched);
        /* Optional<Week> week = weekRepository.findByMonday(mondayFounded);
        if (week.isPresent()) {
            Set<Screening> screenings = week.get().getScreenings();
            Optional<Set<MovieScreeningDTO>> movieScreenings = Utils.createMovieScreeningsFromScreenings(screenings);
            return movieScreenings.orElse(new HashSet<>());
        } */
        return new HashSet<>();

    }


    public Set<MovieScreeningDTO> findMovieScreeningsOfTheLastWeek() {
        /*
        Week week = weekRepository.findFirstByOrderByMondayDesc();
        Set<Screening> screenings = week.getScreenings();
        Optional<Set<MovieScreeningDTO>> movieScreenings = Utils.createMovieScreeningsFromScreenings(screenings);
            */
        return new HashSet<>();


    }

    private List<Screening> getScreeningsOfMonday(LocalDate monday) {
        List<Screening> screenings = new ArrayList<>();
        List<Screening> screeningsTemp = screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday,1);
        List<Screening> screeningsTemp2 = screeningRepository.findScreeningByFirstDayAndNumberOfWeeks(monday.minusDays(7),2);

        screenings.addAll(screeningsTemp);
        screenings.addAll(screeningsTemp2);
        return screenings;

    }

}
