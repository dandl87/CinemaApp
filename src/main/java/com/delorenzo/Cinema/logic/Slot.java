package com.delorenzo.Cinema.logic;

import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.entity.Screening;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Slot {

    private Screening screening;
    private Room room;

    public Slot() {
    }

}
