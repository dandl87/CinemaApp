package com.delorenzo.Cinema.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return imax == movie.imax && Double.compare(value, movie.value) == 0 && Objects.equals(title, movie.title) && Objects.equals(director, movie.director) && Objects.equals(year, movie.year) && Objects.equals(duration, movie.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, director, year, duration, imax, value);
    }
}
