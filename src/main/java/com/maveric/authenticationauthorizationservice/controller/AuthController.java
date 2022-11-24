package com.maveric.authenticationauthorizationservice.controller;

import com.maveric.authenticationauthorizationservice.constant.ErrorMessageConstant;
import com.maveric.authenticationauthorizationservice.dto.AuthRequest;
import com.maveric.authenticationauthorizationservice.dto.AuthResponse;
import com.maveric.authenticationauthorizationservice.feignclient.UserFeignService;
import com.maveric.authenticationauthorizationservice.model.User;
import com.maveric.authenticationauthorizationservice.service.JWTService;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserFeignService userFeignService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> createAuthToken(@RequestBody AuthRequest authRequest){
        User user = null;

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));

        }catch (BadCredentialsException badCredentialsException){
           throw new BadCredentialsException(ErrorMessageConstant.EMAIL_PASSWORD_ERROR);
        }

        ResponseEntity<User> objectResponseEntity = userFeignService.getUserByEmail(authRequest.getEmail());
        user = objectResponseEntity.getBody();

        final String jwt = jwtService.generateToken(user);

        AuthResponse authResponse = getAuthResponse(jwt , user);

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
    @PostMapping("/auth/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody User user) {
        ResponseEntity<User> objectResponseEntity = userFeignService.createUser(user);

        final String jwt = jwtService.generateToken(objectResponseEntity.getBody());

        AuthResponse authResponse = getAuthResponse(jwt ,objectResponseEntity.getBody());

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @GetMapping("/auth/validateToken")
    public ResponseEntity<ConnValidationResponse> validateGet(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name())
                .userId(userId)
                .isAuthenticated(true).build());
    }

    @Getter
    @Builder
    @ToString
    public static class ConnValidationResponse {
        private String status;
        private boolean isAuthenticated;
        private String methodType;
        private String userId;
    }


    public AuthResponse getAuthResponse(String token ,User user){
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUser(user);
        authResponse.setToken(token);

        return authResponse;

    }


}
