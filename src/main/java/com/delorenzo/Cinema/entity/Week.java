package com.delorenzo.Cinema.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "weeks")
@Data
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Temporal(TemporalType.DATE)
    private LocalDate monday;


    @OneToMany(mappedBy = "week",
            cascade = CascadeType.PERSIST)
    private Set<Screening> screenings = new HashSet<>();

    public Week() {
    }

    public void setScreenings(Set<Screening> screenings) {
        this.screenings = screenings;
        for (Screening screening : screenings) {
            screening.setWeek(this);
        }
    }
}
