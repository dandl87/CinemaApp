package com.delorenzo.Cinema.repository;

import com.delorenzo.Cinema.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findScreeningByFirstDayAndNumberOfWeeks(LocalDate firstDay, int numberOfWeeks);
    List<Screening> findScreeningByFirstDay(LocalDate firstDay);
}
