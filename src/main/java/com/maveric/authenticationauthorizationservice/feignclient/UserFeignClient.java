package com.maveric.authenticationauthorizationservice.feignclient;

import com.maveric.authenticationauthorizationservice.dto.UserResponse;
import com.maveric.authenticationauthorizationservice.repository.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @PostMapping("api/v1/users")
    UserResponse createUser(@RequestBody User user);
}
