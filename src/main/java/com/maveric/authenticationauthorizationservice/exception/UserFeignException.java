package com.maveric.authenticationauthorizationservice.exception;

public class UserFeignException extends RuntimeException{
    public UserFeignException(String message){
        super(message);
    }
}
