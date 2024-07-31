package com.TheMFG.HotelManagement.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email Boş Bırakılamaz")
    private String email;

    @NotBlank(message = "Şifre Boş Bırakılamaz")
    private String password;
}
