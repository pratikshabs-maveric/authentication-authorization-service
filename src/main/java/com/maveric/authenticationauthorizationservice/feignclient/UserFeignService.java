package com.maveric.authenticationauthorizationservice.feignclient;

import com.maveric.authenticationauthorizationservice.model.User;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "feignUser",url = "http://localhost:3005/api/v1",configuration = FeignCustomErrorDecoder.class)
public interface UserFeignService {

    @GetMapping("/users/getUserByEmail/{emailId}")
    ResponseEntity<User> getUserByEmail(@PathVariable String emailId);

    @PostMapping("/users")
    ResponseEntity<User> createUser(@RequestBody User user);
}
