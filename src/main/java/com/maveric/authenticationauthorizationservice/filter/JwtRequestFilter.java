package com.maveric.authenticationauthorizationservice.filter;

import com.maveric.authenticationauthorizationservice.feignclient.UserFeignService;
import com.maveric.authenticationauthorizationservice.model.User;
import com.maveric.authenticationauthorizationservice.service.JWTService;
import com.maveric.authenticationauthorizationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFeignService userFeignService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authoriazationHeader = request.getHeader("Authorization");

        String emailId = null;
        String jwt = null;
            if (authoriazationHeader != null && authoriazationHeader.startsWith("Bearer ")) {
                jwt = authoriazationHeader.substring(7);
                emailId = jwtService.getUserEmail(jwt);
            }

            if (emailId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.loadUserByUsername(emailId);
                if (jwtService.validateToken(jwt, userDetails)) {
                    ResponseEntity<User> objectResponseEntity = userFeignService.getUserByEmail(emailId);
                    User getUserIdFromUser = objectResponseEntity.getBody();
                    request.setAttribute("userId", getUserIdFromUser.getId());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        filterChain.doFilter(request,response);
    }
}
