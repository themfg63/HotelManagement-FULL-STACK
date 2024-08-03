package com.TheMFG.HotelManagement.service.Interface;

import com.TheMFG.HotelManagement.dto.Response.Response;
import com.TheMFG.HotelManagement.model.Booking;

public interface BookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
