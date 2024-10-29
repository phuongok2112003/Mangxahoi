//package com.example.Mangxahoi.configuration;
//
//import com.example.Mangxahoi.dto.Otp;
//import com.example.Mangxahoi.dto.TokenDto;
//import com.example.Mangxahoi.entity.UserEntity;
//import com.example.Mangxahoi.repository.UserRepository;
//import com.example.Mangxahoi.services.UserService;
//import com.example.Mangxahoi.utils.EbsConvertUtils;
//import com.example.Mangxahoi.utils.EbsTokenUtils;
//import com.example.Mangxahoi.utils.RenderCodeTest;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//import java.io.IOException;
//
//public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final UserRepository userRepository;
//
//    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
//        this.userRepository = userRepository;
//
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        UserEntity user = (UserEntity) authentication.getPrincipal();
//
//        Otp otp= Otp.builder()
//                .code( RenderCodeTest.setValue())
//                .email(user.getEmail())
//                .build();
//        user.setOtp(otp.getCode());
//        userRepository.save(user);
//        response.getWriter().write(EbsConvertUtils.toString(otp));
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    }
//}
