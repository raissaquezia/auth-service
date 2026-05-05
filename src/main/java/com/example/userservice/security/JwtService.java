package com.example.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.userservice.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:my-secret-key}")
    private String secret;

    @Value("${jwt.access-token-expiration:900}") // 15 min
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800}") // 7 dias
    private long refreshTokenExpiration;

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getLogin())
                .withClaim("userId", user.getId().toString())
                .withClaim("clientId", user.getClientId())
                .withClaim("role", user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getLogin())
                .withClaim("userId", user.getId().toString())
                .withClaim("clientId", user.getClientId())
                .withClaim("type", "REFRESH")
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT validateToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

    public String getSubject(String token) {
        return validateToken(token).getSubject();
    }

    public String getClaim(String token, String claim) {
        return validateToken(token).getClaim(claim).asString();
    }

    public long getAccessTokenExpiration() { return accessTokenExpiration; }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
