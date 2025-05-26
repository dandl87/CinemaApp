package com.delorenzo.Cinema.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String name;
    @Getter
    private Integer seats;
    @Getter
    private Boolean imax;

    public Room(Long id, String name, Integer seats, Boolean imax){
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.imax = imax;
    }

    public Room() {

    }


    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seats=" + seats +
                ", imax=" + imax + '}';
    }
}
