package com.TheMFG.HotelManagement.repository;

import com.TheMFG.HotelManagement.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
