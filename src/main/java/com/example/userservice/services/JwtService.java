package com.example.userservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.userservice.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);

            Instant expirationDate = Instant.now().plusMillis(user.getClient().getTokenExpirationMillis());

            return JWT.create()
                    .withIssuer("auth-service")
                    .withSubject(user.getLogin())
                    .withClaim("role", user.getRole())
                    .withAudience(user.getClient().getId().toString())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro interno ao gerar o token JWT", exception);
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-service")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}