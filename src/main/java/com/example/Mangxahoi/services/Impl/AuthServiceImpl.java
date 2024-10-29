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

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    @Override
    public Otp login(LoginRequest loginRequest)  {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            if (!isValidEmail(username)) {
              throw new EOException(UserStatus.EMAIL_IS_WRONG_FORMAT);
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserEntity user = (UserEntity) authentication.getPrincipal();

            Otp otp= Otp.builder()
                    .code( RenderCodeTest.setValue())
                    .email(user.getEmail())
                    .build();
            user.setOtp(otp.getCode());
            userRepository.save(user);
            return otp;
        } catch (AuthenticationException e) {
           hasException(e);
          return null;
        }
    }
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private void hasException(AuthenticationException exception) {
        if (exception.getCause() instanceof ProviderNotFoundException) {
            throw new EOException((CommonStatus.ACCOUNT_NOT_FOUND));
        }

        if (exception.getCause() instanceof LockedException) {
            throw new EOException(CommonStatus.TEMPORARY_LOCK_NOT_FINISH);
        }

        if (exception instanceof LockedException) {
            throw new EOException(CommonStatus.ACCOUNT_HAS_BEEN_LOCKED);
        }

        if (exception instanceof DisabledException) {
            throw new EOException(CommonStatus.ACCOUNT_IS_NOT_ACTIVATED);
        }
        throw new EOException((CommonStatus.ACCOUNT_NOT_FOUND));

    }
}
