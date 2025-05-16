package com.delorenzo.Cinema.conf;

import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.RoomRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class SchedulerDataLoader implements ApplicationRunner {

    private final Scheduler imaxScheduler;
    private final Scheduler regularScheduler;
    private final RoomRepository roomRepository;


    public SchedulerDataLoader(Scheduler imaxScheduler, Scheduler regularScheduler, RoomRepository roomRepository) {
        this.imaxScheduler = imaxScheduler;
        this.regularScheduler = regularScheduler;
        this.roomRepository = roomRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Room> imaxRooms = roomRepository.findAllByImax(true);
        List<Room> regularRooms = roomRepository.findAllByImax(false);
        imaxScheduler.setRoomsToSlots(imaxRooms);
        regularScheduler.setRoomsToSlots(regularRooms);

    }
}
