package com.example.Mangxahoi.services.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Mangxahoi.constans.MessageCodes;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.constans.enums.Variables;
import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.dto.request.PasswordResetRequest;
import com.example.Mangxahoi.dto.request.Recipient;
import com.example.Mangxahoi.dto.request.SendEmailRequest;
import com.example.Mangxahoi.dto.request.UserRequest;
import com.example.Mangxahoi.dto.response.EmailResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.UserStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.EmailService;
import com.example.Mangxahoi.services.UserService;

import com.example.Mangxahoi.utils.EbsTokenUtils;
import com.example.Mangxahoi.utils.RenderCodeTest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.Date;


import static com.example.Mangxahoi.constans.ErrorCodes.ENTITY_NOT_FOUND;

import static com.example.Mangxahoi.constans.ErrorCodes.ERROR_CODE;
import static com.example.Mangxahoi.services.mapper.UserMapper.entityToDto;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;;

    @Override
    public UserResponseDto register(UserRequest dto) {
        dto.setUsername(dto.getUsername());
        this.validateDto(dto);
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new EOException(UserStatus.PASSWORD_IS_EMPTY);
        }
        UserEntity user = new UserEntity();
        dtoToEntiy(dto,user);
        user.setRole(UserRole.USER);
        user = userRepository.save(user);
        return entityToDto(user);
    }
    @PostAuthorize("returnObject.username == authentication.name")
    @Override
    public UserResponseDto update(@NonNull Long id, UserRequest dto) {
        UserEntity entity=userRepository.findById(id).orElseThrow(() -> new EOException(ENTITY_NOT_FOUND,
                MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
        if(!entity.getUsername().equals(dto.getUsername())){
            this.validateDto(dto);
        }
        dtoToEntiy(dto,entity);
        entity.setActive(dto.isActive());
        entity.setUpdatedAt(Instant.now());
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

    @Override
    public TokenDto getToken(Otp otp) {
        UserEntity user =  userRepository.findByEmail(otp.getEmail());
        if(user.getOtp().equals(otp.getCode())){
            String accessToken = EbsTokenUtils.createAccessToken(user);
            String refreshToken = EbsTokenUtils.createRefreshToken(user.getUsername());

            return new TokenDto(accessToken, refreshToken);
        }
       throw new EOException(UserStatus.WRONG_OTP);

    }

    private void validateDto(UserRequest dto) {
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

    @Override
    public TokenDto refreshToken(String refreshToken) {
        Algorithm algorithm = Algorithm.HMAC256(Variables.SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String email = decodedJWT.getSubject();
        UserEntity user = userRepository.findByEmail(email);

        if (null == user) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }

        String accessToken = EbsTokenUtils.createAccessToken(user);
        return new TokenDto(accessToken, refreshToken);
    }

    @Override
    public EmailResponse sendPasswordResetCode(String email) {
        String code= RenderCodeTest.setValue();
        UserEntity user=userRepository.findByEmail(email);
        if(user==null){
            throw new EOException(ENTITY_NOT_FOUND,
                    MessageCodes.EMAIL_NOT_FOUND, String.valueOf(email));
        }
        String token=  EbsTokenUtils.createCode(code,email);
        user.setOtp(token);
        userRepository.save(user);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        // Xây dựng URL reset password với token
        String resetPasswordLink = baseUrl + "/user/reset-password?token=" + token;


        String htmlContent = "<h1>Reset Your Password</h1> "
                + "<p>Click the link below to reset your password:</p> "
                + "<a href=\"" + resetPasswordLink + "\">Reset Password</a>";


        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .to(Recipient.builder()
                        .email(email)
                        .build())
                .subject("Reset Password")
                .htmlContent(htmlContent)
                .build();

        return emailService.sendEmail(sendEmailRequest);
    }

    @Override
    public String verifyPasswordResetCode(String token,PasswordResetRequest passwordResetRequest) {

        if(passwordResetRequest==null||token==null){
            throw new EOException(CommonStatus.PASSWORD_NOT_CONFIRM);
        }

        try{
            Algorithm algorithm = Algorithm.HMAC256(Variables.SECRET_KEY.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            String email = decodedJWT.getSubject();
            UserEntity user = userRepository.findByEmail(email);
            if(user==null){
                throw new EOException(ENTITY_NOT_FOUND,
                        MessageCodes.EMAIL_NOT_FOUND, String.valueOf(email));
            }


            String pasword1=passwordResetRequest.getPassword();
            String pasword2=passwordResetRequest.getPassword2();
            if(token.equals(user.getOtp())&&pasword1.equals(pasword2)){
                user.setPassword(bCryptPasswordEncoder.encode(pasword1));
                userRepository.save(user);
                return MessageCodes.PROCESSED_SUCCESSFULLY;
            }
            else{
                throw new EOException(ERROR_CODE,
                        MessageCodes.USER_NOT_VERIFY,email);
            }
        }catch (TokenExpiredException ex){
            throw new EOException(CommonStatus.TokenExpired);
        }

    }

    private  void dtoToEntiy(UserRequest userRequestDto, UserEntity userEntity) {
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setRole(userRequestDto.getRole());
        userEntity.setOccupation(userRequestDto.getOccupation());
        userEntity.setLocation(userRequestDto.getLocation());
        userEntity.setAboutMe(userRequestDto.getAboutMe());
        userEntity.setGender(userRequestDto.getGender());
        userEntity.setActive(userRequestDto.isActive());
        userEntity.setCreatedAt(Instant.now());
        userEntity.setDateBirth(userRequestDto.getDateBirth());
        userEntity.setUpdatedAt(Instant.now());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));

    }

}
