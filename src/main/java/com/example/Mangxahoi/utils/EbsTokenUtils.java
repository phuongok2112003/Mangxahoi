package com.example.Mangxahoi.utils;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.Mangxahoi.entity.UserEntity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

import static com.example.Mangxahoi.constans.enums.Variables.*;


public class EbsTokenUtils {
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());

    public static String createAccessToken(UserEntity user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("username",user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static String createRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                .sign(algorithm);
    }
    public static String createCode(String code,String email ) {
        return JWT.create()
                .withSubject(email)
                .withClaim("code", code)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_CODE))
                .sign(algorithm);
    }
}
