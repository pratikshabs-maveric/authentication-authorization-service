package com.maveric.authenticationauthorizationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.authenticationauthorizationservice.constant.Gender;
import com.maveric.authenticationauthorizationservice.controller.AuthController;
import com.maveric.authenticationauthorizationservice.feignclient.UserFeignService;
import com.maveric.authenticationauthorizationservice.model.User;
import com.maveric.authenticationauthorizationservice.service.JWTService;
import com.maveric.authenticationauthorizationservice.service.UserService;
import feign.FeignException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockHttpServletRequestDsl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class,  excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Tag("Integration tests")
class AuthenticationControllerTest {

    private static final String API_V1_AUTH = "http://localhost:3000/api/v1/auth";
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserFeignService userFeignService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;


    @Test
    void shouldReturnSuccessWhenTokenIsValid() throws Exception {
        String bearerToken = "Bearer eyJhbGciOiJIUzI1NJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImV4cCI6MTY2MzI1NzA5MCwiaWF0IjoxNjYzMjIxMDkwfQ.wQl4ssfFUiRtqpsbVYGtFT1kS7MFMI6PwSJc3K5Jw2M";
        mvc.perform(get(API_V1_AUTH+"/validateToken").
                                header("Authorization", bearerToken)).
                andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnTokenWhenCreateTokenInvoked() throws  Exception{
       when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).
               thenReturn(authentication);
        when(userFeignService.getUserByEmail(any())).thenReturn(getSampleUser());
        mvc.perform(get(API_V1_AUTH+"/login")).
                andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnTokenWhenSignupInvoked() throws  Exception{
        when(userFeignService.createUser(any(User.class))).thenReturn(getSampleUser());
        when(jwtService.generateToken(any(User.class))).
                thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphNjNAZ21haWwuY29tIiwiZXhwIjoxNjYzNjkyNzQ3LCJpYXQiOjE2NjM2NTY3N");
        mvc.perform(post(API_V1_AUTH+"/signup").
                        contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getSampleUser()))).
                andExpect(status().isOk())
                .andDo(print());
    }

    public ResponseEntity<User> getSampleUser(){
        User user = new User();
        user.setFirstName("Sneha");
        user.setLastName("Samane");
        user.setEmail("sneha@gmail.com");
        user.setPassword("sneha");
        user.setGender(Gender.FEMALE);
        user.setDateOfBirth("2022-02-02");
        user.setAddress("America");
        user.setPhoneNumber("7744001665");

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
