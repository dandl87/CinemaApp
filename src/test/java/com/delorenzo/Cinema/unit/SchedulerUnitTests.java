package com.delorenzo.Cinema.unit;

import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.logic.Slot;
import com.delorenzo.Cinema.resolvers.MovieParameterResolver;
import com.delorenzo.Cinema.resolvers.RoomParameterResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({RoomParameterResolver.class, MovieParameterResolver.class})
public class SchedulerUnitTests {
    com.delorenzo.Cinema.logic.Scheduler scheduler;


    @BeforeEach
    void init(Map<String,Room> rooms){
        scheduler = new com.delorenzo.Cinema.logic.Scheduler("Scheduler-Test", 5);
        List<Room> roomList = rooms.values().stream().toList();
        scheduler.setRoomsToSlots(roomList);
    }

    @Test
    void schedulerValuesOnCreation() {
        List<Screening> screenings = scheduler.getScheduledScreenings();
        assertAll("scheduler values",
                () -> assertEquals("Scheduler-Test", scheduler.getName()),
                () -> assertEquals(5,scheduler.getSlots().length),
                () -> assertNotNull(scheduler.getScheduledScreenings()),
                () -> assertEquals(5, scheduler.getScheduledScreenings().size()),
                () -> assertEquals(5, scheduler.getFreeRooms())
                );

        assertTrue(screenings.stream().allMatch(Objects::isNull));
    }
    @Test
    void roomsAfterDefined() {
        assertTrue(Arrays.stream(scheduler.getSlots()).toList().stream().allMatch(Objects::nonNull));
        for(Slot slot : scheduler.getSlots() ){
            assertTrue(slot.getRoom().getName().startsWith("Sala "));
            assertTrue(slot.getRoom().getSeats() > 0 );
            assertFalse(slot.getRoom().getImax());
        }
    }

    @Test
    void schedulingMovies(Map<String, Movie> movies){
        scheduler.scheduling(movies.values().stream().toList());
        List<Screening> screenings = scheduler.getScheduledScreenings();
        assertAll(
                () -> assertEquals(0, scheduler.getFreeRooms()),
                () -> assertEquals(5,scheduler.getScheduledScreenings().size())
        );
        screenings.forEach(Assertions::assertNotNull);
        screenings.forEach(screening -> assertNotEquals(screening.getMovie().getTitle(), "Il Padrino 3")
        );
        // il film deve essere schedulato perchè ha valore 10
        Movie newMovie = new Movie(9L,"Dune","D. Lynch", "1970",140,false,10.0);
        scheduler.scheduling( List.of(newMovie) );
        screenings = scheduler.getScheduledScreenings();
        List<Movie> moviesScheduled = new ArrayList<>();
        for (Screening screening : screenings) {
            moviesScheduled.add(screening.getMovie());
        }
        assertTrue(moviesScheduled.contains(newMovie),"Should be present Dune");


    }

}
