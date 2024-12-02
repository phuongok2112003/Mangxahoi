package com.example.Mangxahoi.services.Impl;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.UserRole;
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
import com.example.Mangxahoi.exceptions.EntityNotFoundException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.EmailService;
import com.example.Mangxahoi.services.UserService;
import com.example.Mangxahoi.utils.SecurityUtils;
import com.example.Mangxahoi.utils.TokenUtils;
import com.example.Mangxahoi.utils.RenderCodeTest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.Mangxahoi.constans.ErrorCodes.ENTITY_NOT_FOUND;
import static com.example.Mangxahoi.constans.ErrorCodes.ERROR_CODE;
import static com.example.Mangxahoi.services.mapper.UserMapper.entityToDto;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisTemplate<Object, Object> template;

    @Override
    public UserResponseDto register(UserRequest dto) {

        this.validateDto(dto);
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new EOException(UserStatus.PASSWORD_IS_EMPTY);
        }
        UserEntity user = new UserEntity();
        dtoToEntiy(dto, user);
        user.setRole(UserRole.USER);
        user = userRepository.save(user);
        return entityToDto(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    @Override
    public UserResponseDto update(@NonNull Long id, UserRequest dto) {
        UserEntity entity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(UserEntity.class.getName(), "id", id.toString()));
        if (!entity.getUsername().equals(dto.getEmail())) {
            this.validateDto(dto);
        }
        dtoToEntiy(dto, entity);
        entity.setActive(dto.isActive());
        entity.setUpdatedAt(Instant.now());
        userRepository.save(entity);
        return entityToDto(entity);
    }

    @Override
    public String delete(Long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(UserEntity.class.getName(), "id", id.toString()));
        entity.setActive(false);
        userRepository.save(entity);
        return MessageCodes.PROCESSED_SUCCESSFULLY;
    }

    @Override
    public TokenDto getToken(Otp otp) {


        UserEntity entity = userRepository.findByEmail(otp.getEmail());
        if (entity == null) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        } else {

                Otp codeOTP = (Otp) template.opsForValue().get(otp.getEmail());

                if (codeOTP==null) {

                    throw new EOException(CommonStatus.ACCOUNT_NOT_OTP);
                }

                if (codeOTP.getOtp().equals(otp.getOtp()) && codeOTP.getEmail().equals(entity.getEmail())) {
                    String accessToken = TokenUtils.createAccessToken(entity);

                    return new TokenDto(accessToken);
                }

            throw new EOException(UserStatus.WRONG_OTP);

        }


    }

    @Override
    public UserResponseDto getUser() {
        String  email = SecurityUtils.getEmail();
        UserEntity user=userRepository.findByEmail(email);

        return entityToDto(user);

    }

    private void validateDto(UserRequest dto) {
        if (userRepository.findByEmail(dto.getEmail()) != null) {
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
    public EmailResponse sendPasswordResetCode(String email) {
        String code = RenderCodeTest.setValue();
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EOException(ENTITY_NOT_FOUND,
                    MessageCodes.EMAIL_NOT_FOUND, String.valueOf(email));
        }
        String token = TokenUtils.createCode(code, email);


        Otp otp= Otp.builder()
                .otp( token)
                .email(user.getEmail())
                .build();
        template.opsForValue().set(otp.getEmail(), otp, 5, TimeUnit.MINUTES);

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
    public String verifyPasswordResetCode(String token, PasswordResetRequest passwordResetRequest) {

        if (passwordResetRequest == null || token == null) {
            throw new EOException(CommonStatus.PASSWORD_NOT_CONFIRM);
        }

        try {
            Map<String, Object> claims = TokenUtils.verifyToken(token);
            String email = claims.get("email").toString();
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new EOException(ENTITY_NOT_FOUND,
                        MessageCodes.EMAIL_NOT_FOUND, String.valueOf(email));
            }
            Otp codeOTP = (Otp) template.opsForValue().get(email);

            if (codeOTP==null) {

                throw new EOException(CommonStatus.ACCOUNT_NOT_OTP);
            }

            String pasword1 = passwordResetRequest.getPassword();
            String pasword2 = passwordResetRequest.getPassword2();
            if (token.equals(codeOTP.getOtp()) && pasword1.equals(pasword2)) {
                user.setPassword(bCryptPasswordEncoder.encode(pasword1));
                userRepository.save(user);
                return MessageCodes.PROCESSED_SUCCESSFULLY;
            } else {
                throw new EOException(ERROR_CODE,
                        MessageCodes.USER_NOT_VERIFY, email);
            }
        } catch (TokenExpiredException ex) {
            throw new EOException(CommonStatus.TokenExpired);
        } catch (JWTVerificationException ex) {
            throw new EOException(CommonStatus.TokenIsInvalid);
        }

    }

    @Override
    public UserResponseDto getUser(Long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(UserEntity.class.getName(), "id", id.toString()));
        return entityToDto(entity);
    }

    private void dtoToEntiy(UserRequest userRequestDto, UserEntity userEntity) {
        userEntity.setFullName(userRequestDto.getFullName());
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
