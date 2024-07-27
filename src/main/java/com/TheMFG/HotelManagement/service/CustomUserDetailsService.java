package com.TheMFG.HotelManagement.service;

import com.TheMFG.HotelManagement.exception.OurException;
import com.TheMFG.HotelManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByEmail(username).orElseThrow(() -> new OurException("Kullanıcı Adı / Email bulunamadı"));
    }
}
