package com.maveric.authenticationauthorizationservice.exception;


import com.maveric.authenticationauthorizationservice.dto.Error;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
 class AuthenticationExceptionTest {
    @InjectMocks
    private GlobalExceptionAdvice globalExceptionAdvice;

    @Test
    void handleInvalidPasswordOrEmail() {
        BadCredentialsException exception = new BadCredentialsException("Email or password wrong");
        ResponseEntity<Error> error = globalExceptionAdvice.handleInvalidPasswordOrEmail(exception);
        assertEquals("400 BAD_REQUEST",error.getBody().getCode());
    }
}
