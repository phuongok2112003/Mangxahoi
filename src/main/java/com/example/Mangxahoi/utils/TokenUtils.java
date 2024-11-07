package com.example.Mangxahoi.utils;



import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Mangxahoi.entity.UserEntity;

import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.DataError;
import com.example.Mangxahoi.exceptions.EOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.Mangxahoi.constans.enums.Variables.*;


public class TokenUtils {
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

    public static String createRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
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
    public static Map<String, Object> verifyToken(String token) {
        Map<String, Object> claimsMap = new HashMap<>();

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());

            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            claimsMap.put("email", decodedJWT.getSubject());
            claimsMap.put("id", decodedJWT.getClaim("id").asLong());
            claimsMap.put("username", decodedJWT.getClaim("username").asString());
            claimsMap.put("roles", decodedJWT.getClaim("roles").asArray(String.class));
            return claimsMap;


    }

}
