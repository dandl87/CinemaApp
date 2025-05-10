package com.delorenzo.Cinema.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class MovieScreening {
    private long id;
    private String title;
    private String director;
    private String year;
    private Integer duration;
    private String room;
    private Integer seatsAvailable;
    private LocalDate dateOfStart;
    private LocalDate dateOfEnd;

    public MovieScreening() {
    }

    @Override
    public String toString() {
        return "MovieScreening{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", year='" + year + '\'' +
                ", duration=" + duration +
                ", room='" + room + '\'' +
                ", seatsAvailable=" + seatsAvailable +
                ", dateOfStart=" + dateOfStart +
                ", dateOfEnd=" + dateOfEnd +
                '}';
    }
}
