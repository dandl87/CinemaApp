package com.delorenzo.Cinema.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
public class Movie{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String director;
    private String year;
    private Integer duration;
    private boolean imax;
    private double value;

    @OneToMany(mappedBy = "movie",
                cascade = CascadeType.PERSIST,
                fetch = FetchType.EAGER)
    private List<Screening> screenings = new ArrayList<>();

}
