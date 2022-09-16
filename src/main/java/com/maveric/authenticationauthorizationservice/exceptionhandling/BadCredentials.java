package com.maveric.authenticationauthorizationservice.exceptionhandling;

public class BadCredentials extends RuntimeException{
    public BadCredentials(String msg){
        super(msg);
    }
}

