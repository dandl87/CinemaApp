package com.delorenzo.Cinema.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "screenings")
public class Screening implements Cloneable {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "week_id")
    private Week week;

    @Getter
    @Setter
    private int numberOfWeeks;


    public Screening() {
    }

    public Screening(Room room, Movie movie,Week week) {
        this.room = room;
        this.movie = movie;
        this.week = week;
    }


    @Override
    public Screening clone() {
        Screening s = new Screening();
        s.setRoom(room);
        s.setMovie(movie);
        s.setWeek(week);
        s.setNumberOfWeeks(numberOfWeeks);
        return s;
    }


}
