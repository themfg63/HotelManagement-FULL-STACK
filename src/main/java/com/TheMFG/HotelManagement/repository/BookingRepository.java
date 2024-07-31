package com.TheMFG.HotelManagement.repository;

import com.TheMFG.HotelManagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
