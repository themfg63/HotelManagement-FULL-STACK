package com.TheMFG.HotelManagement.service.Interface;

import com.TheMFG.HotelManagement.dto.request.LoginRequest;
import com.TheMFG.HotelManagement.model.User;
import org.apache.coyote.Response;

public interface UserService {
    Response register(User loginRequest);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(String userId);
    Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getMyInfo(String userId);
}
