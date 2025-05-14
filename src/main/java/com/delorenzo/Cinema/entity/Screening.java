package com.delorenzo.Cinema.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "screenings")
public class Screening implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private LocalDate firstDay;

    private int numberOfWeeks;


    public Screening() {
    }

    public Screening(Room room, Movie movie,LocalDate firstDay) {
        this.room = room;
        this.movie = movie;
        this.firstDay = firstDay;
    }


    @Override
    public Screening clone() {
        Screening s = new Screening();
        s.setRoom(room);
        s.setMovie(movie);
        s.setFirstDay(firstDay);
        s.setNumberOfWeeks(numberOfWeeks);
        return s;
    }

    public boolean sameMovie(Screening screening){
        return this.movie.equals(screening.getMovie());
    }

    @Override
    public String toString() {
        return "Screening{" +
                "id=" + id +
                ", room=" + room.getName() +
                ", movie=" + movie.getTitle() +
                ", firstDay=" + firstDay +
                ", numberOfWeeks=" + numberOfWeeks +
                '}';
    }
}
