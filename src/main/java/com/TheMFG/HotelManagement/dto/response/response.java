package com.TheMFG.HotelManagement.dto.response;

import com.TheMFG.HotelManagement.dto.BookingDTO;
import com.TheMFG.HotelManagement.dto.RoomDTO;
import com.TheMFG.HotelManagement.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class response {
    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;
    private UserDTO user;
    private RoomDTO room;
    private BookingDTO booking;
    private List<UserDTO> userList;
    private List<RoomDTO> roomList;
    private List<BookingDTO> bookingList;
}
