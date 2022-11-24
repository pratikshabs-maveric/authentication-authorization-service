package com.maveric.authenticationauthorizationservice.dto;

import com.maveric.authenticationauthorizationservice.constant.Gender;
import lombok.Data;

@Data
public class UserDto {

    private  String id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String dateOfBirth;

    private Gender gender;
}
