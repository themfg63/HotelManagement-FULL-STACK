package com.TheMFG.HotelManagement.service.Interface;

import com.TheMFG.HotelManagement.dto.Request.LoginRequest;
import com.TheMFG.HotelManagement.dto.Response.Response;
import com.TheMFG.HotelManagement.model.User;

public interface UserService {
    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(String userId);
    Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getMyInfo(String email);
}
