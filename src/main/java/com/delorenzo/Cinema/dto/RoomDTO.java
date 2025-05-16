package com.delorenzo.Cinema.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private String roomName;
    private int seats;

    public RoomDTO() {
    }
    public RoomDTO(String roomName, int seats) {

        this.roomName = roomName;
        this.seats = seats;
    }
}
