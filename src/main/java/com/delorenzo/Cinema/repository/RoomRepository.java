package com.delorenzo.Cinema.repository;

import com.delorenzo.Cinema.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long>{
    List<Room> findAllByImax(boolean b);
}
