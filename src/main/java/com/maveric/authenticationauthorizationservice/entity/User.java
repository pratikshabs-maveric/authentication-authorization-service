package com.maveric.authenticationauthorizationservice.repository.entity;

import com.maveric.authenticationauthorizationservice.Gender.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name="user_details")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long Id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String email;
    private String address;
    private Date dateOfBirth;
    private Gender gender;
    private String password;
}
