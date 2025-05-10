package com.delorenzo.Cinema.logic;

import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.entity.Screening;

public class Slot {

    private Screening screening;
    private Room room;


    public Slot(Screening screening, Room room) {
        this.screening = screening;
        this.room = room;
    }

    public Slot() {
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "screening=" + screening +
                ", room=" + room +
                '}';
    }
}
