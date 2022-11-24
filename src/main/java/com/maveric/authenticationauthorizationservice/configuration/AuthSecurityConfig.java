package com.maveric.authenticationauthorizationservice.configuration;

import com.maveric.authenticationauthorizationservice.feignclient.FeignCustomErrorDecoder;
import com.maveric.authenticationauthorizationservice.filter.JwtRequestFilter;
import com.maveric.authenticationauthorizationservice.service.UserService;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthSecurityConfig {

    @Autowired
    UserService userService;

    @Value("${app.auth-login}")
    private String authUrl ;

    @Value("${app.auth-login-v1}")
    private String authLoginUrl ;

    @Value("${app.auth-signup}")
    private String authSignupUrl ;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtRequestFilter jwtRequestFilter) throws Exception {
        http
                .csrf().disable();
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .authorizeRequests()
                .antMatchers("/error").permitAll();
        http
                .authorizeRequests()
                .antMatchers(authUrl,authLoginUrl,authSignupUrl).permitAll();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .authorizeRequests()
                .anyRequest().authenticated();
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}
