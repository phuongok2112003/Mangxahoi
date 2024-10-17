package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.constans.MessageCodes;

import com.example.Mangxahoi.dto.request.UserRequestDto;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.UserStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;


import static com.example.Mangxahoi.constans.ErrorCodes.ENTITY_NOT_FOUND;

import static com.example.Mangxahoi.services.mapper.UserMapper.entityToDto;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


    @Override
    public UserResponseDto register(UserRequestDto dto) {
        dto.setUsername(dto.getUsername());
        this.validateDto(dto);
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new EOException(UserStatus.PASSWORD_IS_EMPTY);
        }
        UserEntity user = new UserEntity();
        dtoToEntiy(dto,user);
        user = userRepository.save(user);
        return entityToDto(user);
    }

    @Override
    public UserResponseDto update(@NonNull Long id, UserRequestDto dto) {
        UserEntity entity=userRepository.findById(id).orElseThrow(() -> new EOException(ENTITY_NOT_FOUND,
                MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
        if(!entity.getUsername().equals(dto.getUsername())){
            this.validateDto(dto);
        }
        dtoToEntiy(dto,entity);
        entity.setActive(dto.isActive());
        entity.setUpdatedAt(new Date());
        userRepository.save(entity);
        return entityToDto(entity);
    }

    @Override
    public String delete(Long id) {
        UserEntity entity=userRepository.findById(id).orElseThrow(() -> new EOException(ENTITY_NOT_FOUND,
                MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
          entity.setActive(false);
          userRepository.save(entity);
          return "thanh cong";
    }
    private void validateDto(UserRequestDto dto) {
        if (!StringUtils.hasText(dto.getUsername())) {
            throw new EOException(UserStatus.USERNAME_IS_EMPTY);
        }

        if (userRepository.findByEmail(dto.getUsername())!=null) {
            throw new EOException(UserStatus.EMAIL_IS_EXIST);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new ProviderNotFoundException(CommonStatus.ACCOUNT_NOT_FOUND.getMessage());
        }
        return user;
    }
    private  void dtoToEntiy(UserRequestDto userRequestDto, UserEntity userEntity) {
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setRole(userRequestDto.getRole());
        userEntity.setGender(userRequestDto.getGender());
        userEntity.setActive(userRequestDto.isActive());
        userEntity.setCreatedAt(userRequestDto.getCreatedAt());
        userEntity.setDateBirth(userRequestDto.getDateBirth());
        userEntity.setUpdatedAt(userRequestDto.getUpdatedAt());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));

        userEntity.setPosts(userRequestDto.getPosts());
        userEntity.setComments(userRequestDto.getComments());
        userEntity.setLikes(userRequestDto.getLikes());
        userEntity.setFriends(userRequestDto.getFriends());
        userEntity.setImages(userRequestDto.getImages());

    }

}
