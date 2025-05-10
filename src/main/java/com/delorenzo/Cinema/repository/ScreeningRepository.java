package com.delorenzo.Cinema.repository;

import com.delorenzo.Cinema.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScreeningRepository extends JpaRepository<Screening, Long> {

}
