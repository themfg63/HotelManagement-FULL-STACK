package com.TheMFG.HotelManagement.repository;

import com.TheMFG.HotelManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email); // emailin olup olmadığını kontrol eder.
    Optional<User> findByEmail(String email); // belirtilen email User nesnesini getirir.
}
