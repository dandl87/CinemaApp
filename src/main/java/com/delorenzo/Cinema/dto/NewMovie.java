package com.delorenzo.Cinema.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NewMovie {
    private String title;
    private String director;
    private int year;
    private int duration;
    private boolean imax;
    private double value;
    private Date out;
    private String room;

    public NewMovie() {

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
                ", out=" + out +
                ", room='" + room + '\'' +
                '}';
    }
}
