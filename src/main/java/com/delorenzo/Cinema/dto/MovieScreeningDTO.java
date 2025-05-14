package com.delorenzo.Cinema.dto;

import lombok.Data;

@Data
public class MovieScreeningDTO {
    private String title;
    private String director;
    private String year;
    private Integer duration;
    private String room;
    private Integer seatsAvailable;

    public MovieScreeningDTO() {
    }

    @Override
    public String toString() {
        return "MovieScreening{" +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", year='" + year + '\'' +
                ", duration=" + duration +
                ", room='" + room + '\'' +
                ", seatsAvailable=" + seatsAvailable +
                '}';
    }
}
