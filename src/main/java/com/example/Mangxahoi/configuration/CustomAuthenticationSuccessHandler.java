package com.example.Mangxahoi.configuration;

import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.services.UserService;
import com.example.Mangxahoi.utils.EbsConvertUtils;
import com.example.Mangxahoi.utils.EbsTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String accessToken = EbsTokenUtils.createAccessToken(user);
        String refreshToken = EbsTokenUtils.createRefreshToken(user.getUsername());

        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);
        response.getWriter().write(EbsConvertUtils.toString(tokenDto));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
