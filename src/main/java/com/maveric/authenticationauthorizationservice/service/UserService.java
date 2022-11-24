package com.maveric.authenticationauthorizationservice.service;

import com.maveric.authenticationauthorizationservice.feignclient.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserFeignService userFeignService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ResponseEntity<com.maveric.authenticationauthorizationservice.model.User> objectResponseEntity = userFeignService.getUserByEmail(email);
        com.maveric.authenticationauthorizationservice.model.User reqUser = objectResponseEntity.getBody();
        return new User(reqUser.getEmail(), reqUser.getPassword(), new ArrayList<>());
    }
}
