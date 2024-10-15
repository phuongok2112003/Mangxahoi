package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.configuration.JwtTokenBlacklist;
import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.dto.request.PasswordResetRequest;
import com.example.Mangxahoi.dto.request.UserRequestDto;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final JwtTokenBlacklist tokenBlacklist;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserResponseDto getUserDtoByUsername(String username) {
        return null;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return null;
    }

    @Override
    public UserResponseDto getInfo() {
        return null;
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        return null;
    }

    @Override
    public UserResponseDto save(UserRequestDto dto) {
        return null;
    }

    @Override
    public UserResponseDto update(@NonNull int id, UserRequestDto dto) {
        return null;
    }

    @Override
    public Boolean delete(int id) {
        return null;
    }

    @Override
    public boolean permanentLock(String username) {
        return false;
    }

    @Override
    public String sendPasswordResetCode(String email) {
        return "";
    }

    @Override
    public Boolean verifyPasswordResetCode(PasswordResetRequest passwordResetRequest) {
        return null;
    }

    @Override
    public TokenDto refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
