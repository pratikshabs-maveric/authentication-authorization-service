package com.maveric.authenticationauthorizationservice.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Setter
public class AuthenticationRequest {

    private String email;
    private String password;
}
