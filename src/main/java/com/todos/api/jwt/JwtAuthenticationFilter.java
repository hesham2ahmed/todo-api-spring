package com.todos.api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todos.api.model.Person;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtConfig jwtConfig,
                                   SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            );

            return authenticationManager.authenticate(authentication);

        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Person person = (Person) authResult.getPrincipal();

        String token = prepareToken(person, authResult);

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);

//        response.getWriter().print(new ObjectMapper().writeValueAsString(person.getTodos()));
    }

    private String prepareToken(Person person, Authentication authResult){
        return Jwts.builder()
                .setSubject(person.getId())
                .claim("authorities", authResult.getAuthorities())
                .claim("username", person.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
    }
}
