package com.TheMFG.HotelManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email boş bırakılamaz")
    private String email;

    @NotBlank(message = "Şifre boş bırakılamaz")
    private String password;
}
