package com.maveric.authenticationauthorizationservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@Data
public class AuthRequest {
    @Email
    private String email;

    @Min(3)
    private String password;
}
