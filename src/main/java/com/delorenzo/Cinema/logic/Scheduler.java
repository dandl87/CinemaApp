package com.delorenzo.Cinema.logic;

import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.exception.RoomsSlotsMismatchException;
import com.delorenzo.Cinema.utils.Utils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class Scheduler {
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
    private final String name;
    private final Slot[] slots;
    private int freeRooms;

    public Scheduler(String name, int numberOfRooms) {
        logger.info("Scheduler {} creation", name);
        this.name = name;
        this.freeRooms = numberOfRooms;
        this.slots = new Slot[numberOfRooms];
        for (int i = 0; i < numberOfRooms; i++)
            slots[i] = new Slot();
    }


    public void setRoomsToSlots(List<Room> rooms) {
        logger.info("Scheduler {} room assignment " +
                "\n numer of rooms {} " +
                "\n number of slots {} ", name, rooms.size(), getSlots());
        if (getSlots().length != rooms.size())
            throw new RoomsSlotsMismatchException();
        for (int i = 0; i < slots.length; i++) {
            slots[i].setRoom(rooms.get(i));
        }
    }


    public synchronized void scheduling(List<Movie> movies) {
        removeScreenings();
        List<Screening> screenings = Utils.createScreenings(movies);


        // Inserting on empty slot first
        for (Slot slot : slots) {
            Optional<Screening> screeningMaxValue = Utils.extractScreeningWithMaxValue(screenings);

            if ((slot.getScreening() == null) && (!screenings.isEmpty())) {
                slot.setScreening(screeningMaxValue.get());
                screeningMaxValue.get().setRoom(slot.getRoom());
                screenings.remove(screeningMaxValue.get());
                freeRooms--;
                logger.info("Screening of movie {} assigned to empty room  ", screeningMaxValue.get().getMovie().getTitle());
            }
        }
        // and then the remainings ones to replace those of lesser value
        for (Slot slot : slots) {
            Optional<Screening> screeningMaxValue = Utils.extractScreeningWithMaxValue(screenings);

            if ((slot.getScreening() != null) && (!screenings.isEmpty()) && (slot.getScreening().getMovie().getValue() < screeningMaxValue.get().getMovie().getValue())) {
                String movieReplaced = slot.getScreening().getMovie().getTitle();
                slot.setScreening(screeningMaxValue.get());
                screeningMaxValue.get().setRoom(slot.getRoom());
                screenings.remove(screeningMaxValue.get());
                logger.info("Screening of movie {} assigned to room in the place of {}", screeningMaxValue.get().getMovie().getTitle(), movieReplaced);
            }

        }

    }

    public void removeScreenings() {
        for (Slot slot : slots) {
            Optional<Screening> screening = Optional.ofNullable(slot.getScreening());
            if (screening.isPresent()) {
                if (screening.get().getNumberOfWeeks() == 3) {
                    slot.setScreening(null);
                    freeRooms--;
                }
            }
        }
    }

    public List<Screening> getScheduledScreenings() {
        List<Screening> screenings = new ArrayList<>();
        for (Slot slot : slots) {
            screenings.add(slot.getScreening());
        }
        return screenings;
    }
}