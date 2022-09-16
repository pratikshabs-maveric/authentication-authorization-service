package com.maveric.authenticationauthorizationservice.repository;

import com.maveric.authenticationauthorizationservice.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<User, Long> {

    @Query(value ="select * from user_details where email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);
}
