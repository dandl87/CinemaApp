package com.delorenzo.Cinema.repository;

import com.delorenzo.Cinema.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {

    Week findFirstByOrderByMondayDesc();

    Optional<Week> findByMondayBetween(LocalDate monday, LocalDate sameDayNextWeek);

    Optional<Week> findByMonday(LocalDate mondayFounded);
}
