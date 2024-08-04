package com.TheMFG.HotelManagement.service.Impl;

import com.TheMFG.HotelManagement.dto.Request.LoginRequest;
import com.TheMFG.HotelManagement.dto.Response.Response;
import com.TheMFG.HotelManagement.dto.UserDTO;
import com.TheMFG.HotelManagement.exception.OurException;
import com.TheMFG.HotelManagement.model.User;
import com.TheMFG.HotelManagement.repository.UserRepository;
import com.TheMFG.HotelManagement.service.Interface.UserService;
import com.TheMFG.HotelManagement.utils.JWTUtils;
import com.TheMFG.HotelManagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {
        Response response = new Response();

        try{
            if(user.getRole() == null || user.getRole().isBlank()){
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail() + "Email Adresi Zaten Kullanımda");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Kullanıcı Kaydedilirken Bir Hata Oluştu" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new OurException("Kullanıcı Bulunamadı"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Gün");
            response.setMessage("Giriş Başarılı");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Kullanıcı Girişi Sırasında Bir Hata Oluştu" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try{
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Başarılı");
            response.setUserList(userDTOList);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Tüm Kullanıcılar Getirilirken Bir Hata Oluştu" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("Kullanıcı Bulunamadı"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Başarılı");
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Tüm Kullanıcılar Getirilirken Bir Hata Oluştu" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("Kullanıcı Bulunamadı"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("Kullanıcı Silindi!");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Tüm Kullanıcılar Getirilirken Bir Hata Oluştu" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("Kullanıcı Bulunamadı"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Başarılı");
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Tüm Kullanıcılar Getirilirken Bir Hata Oluştu" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("Kullanıcı Bulunamadı"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Başarılı");
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Tüm Kullanıcıları Getirirken Bir Hata Oluştu" + e.getMessage());
        }
        return response;
    }
}
