package com.maveric.authenticationauthorizationservice.dto;

import com.maveric.authenticationauthorizationservice.model.User;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;

    private User user;
}
