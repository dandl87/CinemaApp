package com.delorenzo.Cinema.dto;

import lombok.Data;

@Data
public class MovieScreeningDTO {
    private String title;
    private String year;
    private Integer duration;
    private String room;
    private Integer seatsAvailable;

    public MovieScreeningDTO() {
    }

}
