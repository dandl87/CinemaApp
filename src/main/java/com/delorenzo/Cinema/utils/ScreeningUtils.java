package com.delorenzo.Cinema.utils;

import com.delorenzo.Cinema.dto.RoomScreeningDTO;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Screening;
import java.util.*;


public class ScreeningUtils {


    public static List<Screening> createScreenings(List<Movie> movies) {
        List<Screening> screenings = new ArrayList<>();
        for (Movie movie : movies) {
            Screening screening = new Screening();
            screening.setMovie(movie);
            screening.setNumberOfWeeks(0);
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

    // This method create the List Of Screenings for the next week with the right number of weeks
    public static List<Screening> getScreeningsToBeSavedPreparedForDb(List<Screening> screeningsOfTheWeek, List<Screening> screeningsToBeSaved) {
        List<Screening> screeningsToBeSavedPreparedForDb = new ArrayList<>();
        screeningsToBeSaved.forEach(screening ->
                {
                    Optional<Screening> screeningOpt = extractScreeningFromAList(screening, screeningsOfTheWeek);
                    if (screeningOpt.isPresent()) {
                        screeningOpt.get().setNumberOfWeeks(screeningOpt.get().getNumberOfWeeks() + 1);
                        screeningsToBeSavedPreparedForDb.add(screeningOpt.get());
                    } else
                        screeningsToBeSavedPreparedForDb.add(screening);
                }
        );
        return screeningsToBeSavedPreparedForDb;
    }





}
