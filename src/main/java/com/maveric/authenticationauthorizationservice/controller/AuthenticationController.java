package com.maveric.authenticationauthorizationservice.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maveric.authenticationauthorizationservice.dto.AuthenticationResponse;
import com.maveric.authenticationauthorizationservice.dto.UserResponse;
import com.maveric.authenticationauthorizationservice.repository.entity.User;
import com.maveric.authenticationauthorizationservice.feignclient.UserFeignClient;
import com.maveric.authenticationauthorizationservice.dto.AuthenticationRequest;
import com.maveric.authenticationauthorizationservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.maveric.authenticationauthorizationservice.util.JwtUtil;

@Configuration
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationService service;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserFeignClient feignClient;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody User user)
    {
        UserResponse response = feignClient.createUser(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new ResponseEntity<>(new AuthenticationResponse(token, response), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authRequest)
    {

        UserResponse user = service.getUserByEmail(authRequest.getEmail(), authRequest.getPassword());
        String token = jwtUtil.generateToken(authRequest.getEmail());
        return new ResponseEntity<>(new AuthenticationResponse(token, user), HttpStatus.OK);
    }
}
