package com.example.Mangxahoi.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.example.Mangxahoi.constans.enums.Variables;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Component
public class JwtTokenBlacklist {
    private final Set<String> blacklist = new HashSet<>();

    public void add(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public void removeExpiredTokens() {
        blacklist.removeIf(this::isTokenExpired);
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = getExpirationDateFromToken(token);
            Date now = new Date();
            return expirationDate != null && expirationDate.before(now);
        } catch (Exception e){
            return false;
        }
    }

    private Date getExpirationDateFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(Variables.SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getExpiresAt();
    }
}