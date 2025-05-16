package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.RoomDTO;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomDTO> roomDTOs = new ArrayList<>();
        for (Room room : rooms) {
            roomDTOs.add(new RoomDTO(room.getName(),room.getSeats()));
        }
        return roomDTOs;
    }

}
