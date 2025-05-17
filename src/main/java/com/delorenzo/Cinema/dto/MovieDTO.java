package com.delorenzo.Cinema.dto;

import com.delorenzo.Cinema.entity.Screening;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MovieDTO {
        private String title;
        private String director;
        private String year;
        private int duration;
        private boolean imax;
        private double value;
        private Map<LocalDate, Integer> screeningsDates = new HashMap<>();

        public MovieDTO() {

        }

    public void setScreeningsDates(List<Screening> screenings) {
            for (Screening screening : screenings) {
                screeningsDates.put(screening.getFirstDay(),screening.getNumberOfWeeks());
            }
    }
}
