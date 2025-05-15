package com.delorenzo.Cinema.utils;

import com.delorenzo.Cinema.dto.MovieScreeningDTO;
import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


public class Utils {

    public static Optional<Set<MovieScreeningDTO>> createMovieScreeningsFromScreenings(Set<Screening> screenings) {
        Set<MovieScreeningDTO> movieScreenings = new HashSet<>();
        for (Screening screening : screenings) {
            Movie movie = screening.getMovie();
            Room room = screening.getRoom();
            MovieScreeningDTO movieScreening = new MovieScreeningDTO();
            movieScreening.setRoom(room.getName());
            movieScreening.setTitle(movie.getTitle());
            movieScreening.setYear(movie.getYear());
            movieScreening.setDuration(movie.getDuration());
            movieScreening.setSeatsAvailable(room.getSeats());
            movieScreenings.add(movieScreening);
        }
        if (!movieScreenings.isEmpty()) {
            return Optional.of(movieScreenings);
        } else {
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

    public static List<Screening> createScreenings(List<Movie> movies) {
        List<Screening> screenings = new ArrayList<>();
        for (Movie movie : movies) {
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
            if (result.isPresent())
                return result;
        }
        return Optional.empty();
    }

    public static Optional<Screening> extractScreeningFromAList(Screening screening, List<Screening> screenings) {
        //Screenings are sorted yet
        if (screenings.isEmpty())
            return Optional.empty();
        for (Screening screeningItem : screenings) {
            if (screeningItem.getMovie().getTitle().equals(screening.getMovie().getTitle()))
                return Optional.of(screeningItem);
        }
        return Optional.empty();
    }

    public static List<RoomScreeningDTO> getRoomScreeningDTOList(List<Screening> screenings) {
        List<RoomScreeningDTO> roomScreeningDTOS = new ArrayList<>();
        for (Screening screening : screenings) {
            RoomScreeningDTO roomScreeningDTO = new RoomScreeningDTO();
            roomScreeningDTO.setMovieTitle(screening.getMovie().getTitle());
            roomScreeningDTO.setDuration(screening.getMovie().getDuration());
            roomScreeningDTO.setSeatsAvailable(screening.getRoom().getSeats());
            roomScreeningDTO.setRoomName(screening.getRoom().getName());
            roomScreeningDTO.setImax(screening.getMovie().isImax());
            roomScreeningDTOS.add(roomScreeningDTO);
        }
        roomScreeningDTOS.sort(Comparator.comparing(RoomScreeningDTO::getRoomName));
        return roomScreeningDTOS;
    }



}
