package com.maveric.authenticationauthorizationservice.exceptionhandling;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
