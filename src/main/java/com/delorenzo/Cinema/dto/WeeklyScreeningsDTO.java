package com.delorenzo.Cinema.dto;


import lombok.Data;

@Data
public class WeeklyScreeningsDTO {
    private String roomName;
    private String movieTitle;
    private Integer duration;
    private Integer seatsAvailable;
    private boolean imax;

    public WeeklyScreeningsDTO() {
    }


}
