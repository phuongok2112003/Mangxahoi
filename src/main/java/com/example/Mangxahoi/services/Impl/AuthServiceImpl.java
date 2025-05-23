package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.request.LoginRequest;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.UserStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.AuthService;

import com.example.Mangxahoi.utils.RenderCodeTest;

import com.example.Mangxahoi.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final RedisTemplate<Object,Object> template;
    @Override
    public Otp login(LoginRequest loginRequest)  {
        try {
            String username = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserEntity user = (UserEntity) authentication.getPrincipal();

            Otp otp= Otp.builder()
                    .otp( RenderCodeTest.setValue())
                    .email(user.getEmail())
                    .build();

            template.opsForValue().set( otp.getEmail().toLowerCase(), otp, 5, TimeUnit.MINUTES);

            return otp;
        } catch (AuthenticationException e) {
           hasException(e);
          return null;
        }
    }


    private void hasException(AuthenticationException exception) {
        throw new EOException((CommonStatus.ACCOUNT_NOT_FOUND));

    }


}
