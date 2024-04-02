package com.example.registrationsystemapi.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.registrationsystemapi.domain.user.User;

@Service
public class TokenService {
   
    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
            .withIssuer("registration-system-api")
            .withSubject(user.getEmail())
            .withExpiresAt(this.generateExpirationDate())
            .sign(algorithm);

            return token;
        } catch (Exception e) {
            throw new RuntimeException("Error while authenticating");
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(3));
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
            .withIssuer("registration-system-api")
            .build()
            .verify(token)
            .getSubject();
            
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}
