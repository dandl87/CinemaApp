package com.delorenzo.Cinema.utils;

import com.delorenzo.Cinema.dto.MovieScreening;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public static Optional<Set<MovieScreening>> createMovieScreeningsFromScreenings(Set<Screening> screenings){
        Set<MovieScreening> movieScreenings = new HashSet<>();
        for (Screening screening : screenings) {
            Movie movie = screening.getMovie();
            Room room = screening.getRoom();
            MovieScreening movieScreening = new MovieScreening();
            movieScreening.setId(screening.getId());
            movieScreening.setRoom(room.getName());
            movieScreening.setTitle(movie.getTitle());
            movieScreening.setDirector(movie.getDirector());
            movieScreening.setYear(movie.getYear());
            movieScreening.setDuration(movie.getDuration());
            movieScreening.setSeatsAvailable(room.getSeats());
            movieScreenings.add(movieScreening);
        }
        if(!movieScreenings.isEmpty()){
            return Optional.of(movieScreenings);
        }else {
            return Optional.empty();
        }
    }




    public static LocalDate findTheMondayOfTheWeek(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        LocalDate result = switch (day) {
            case TUESDAY -> date.minusDays(1);
            case WEDNESDAY -> date.minusDays(2);
            case THURSDAY -> date.minusDays(3);
            case FRIDAY -> date.minusDays(4);
            case SATURDAY -> date.minusDays(5);
            case SUNDAY -> date.minusDays(6);
            default -> date;
        };
        return result;
    }

    public static Optional<Scheduler> getSchedulerByName(List<Scheduler> schedulers, String name) {
        for (Scheduler scheduler : schedulers) {
            if (scheduler.getName().equals(name)) {
                return Optional.of(scheduler);
            }
        }
        return Optional.empty();
    }

    public static List<Screening> createScreenings(List<Movie> movies){
        List<Screening> screenings = new ArrayList<>();
        for(Movie movie : movies){
            Screening screening = new Screening();
            screening.setMovie(movie);
            screening.setNumberOfWeeks(1);
            screenings.add(screening);
        }
        screenings.sort(new ScreeningComparator());
        return screenings;
    }

    public static Optional<Screening> extractScreeningWithMaxValue(List<Screening> screenings) {
        //Screenings are sorted yet
        if (!screenings.isEmpty()) {
        Optional<Screening> result = Optional.ofNullable(screenings.getLast());
        if(result.isPresent())
            return result;
        }
        return Optional.empty();
    }
    public static Optional<Screening> extractScreeningWithMinValue(List<Screening> screenings) {
        //Screenings are sorted yet
        if (!screenings.isEmpty()) {
            Optional<Screening> result = Optional.ofNullable(screenings.getFirst());
            if(result.isPresent())
                return result;
        }
        return Optional.empty();
    }
}
