package com.example.Mangxahoi.utils;

import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
@Slf4j
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.isNull(authentication) && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static Object getPrincipal() {
        if (isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getPrincipal();
        }

        return null;
    }

    public static String getEmail() {
        if (isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return ((UserEntity) authentication.getPrincipal()).getEmail();
        }

        throw new EOException(CommonStatus.FORBIDDEN);
    }

    public static UserEntity getCurrentUser() {
        if (isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (UserEntity) authentication.getPrincipal();
        }

        throw new EOException(CommonStatus.FORBIDDEN);
    }
    public static boolean checkUser(String username){
        return username.equals(SecurityUtils.getCurrentUser().getUsername());
    }
}
