package com.maveric.authenticationauthorizationservice.service;

import com.maveric.authenticationauthorizationservice.dto.UserResponse;
import com.maveric.authenticationauthorizationservice.repository.entity.User;
import com.maveric.authenticationauthorizationservice.exceptionhandling.BadCredentials;
import com.maveric.authenticationauthorizationservice.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository userRepo;

    public UserResponse getUserByEmail(String email, String password){
        User user= userRepo.findByEmail(email).orElseThrow(()->
                new BadCredentials("Invalid Credentials"));

        if (!password.equals(user.getPassword())){
            throw new BadCredentials("Invalid Credentials");
        }
        return UserResponse.convertUserToUserDto(user);
    }
}
