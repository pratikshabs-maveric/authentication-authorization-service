package com.maveric.authenticationauthorizationservice.dto;

import com.maveric.authenticationauthorizationservice.Gender.Gender;
import com.maveric.authenticationauthorizationservice.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
@AllArgsConstructor
@Getter
public class UserResponse {

    private long Id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private Gender gender;
    private String password;

    public static UserResponse convertUserToUserDto(User user){
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getPassword()
        );
    }
}
