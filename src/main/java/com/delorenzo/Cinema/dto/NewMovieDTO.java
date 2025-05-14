package com.delorenzo.Cinema.dto;

import lombok.Data;

@Data
public class NewMovieDTO {
    private String title;
    private String director;
    private int year;
    private int duration;
    private boolean imax;
    private double value;
    private String room;

    public NewMovieDTO() {

    }

    @Override
    public String toString() {
        return "NewMovie{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                ", duration=" + duration +
                ", imax=" + imax +
                ", value=" + value +
                ", room='" + room + '\'' +
                '}';
    }
}
