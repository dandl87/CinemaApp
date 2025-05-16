package com.delorenzo.Cinema.conf;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Getter
@Component
public class DateHolder {
    private LocalDate currentDate = LocalDate.now();

    public synchronized void updateDate(LocalDate newDate) {
        this.currentDate = newDate;
    }

}
