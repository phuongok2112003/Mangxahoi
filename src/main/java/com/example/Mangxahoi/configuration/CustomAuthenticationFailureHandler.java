package com.example.Mangxahoi.configuration;



import com.example.Mangxahoi.dto.request.LoginRequest;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.DataError;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.UserService;
import com.example.Mangxahoi.utils.EbsConvertUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final UserRepository userRepository;

    public CustomAuthenticationFailureHandler(UserRepository userService) {
        this.userRepository = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (hasException(response, exception)) return;

        LoginRequest loginRequest = CustomAuthenticationFilter.getLoginRequest(); // Retrieve loginRequest from thread-local variable
        if (loginRequest == null) {
            response.getWriter().write(EbsConvertUtils.toString(DataError.build(CommonStatus.WRONG_USERNAME_OR_PASSWORD)));
            return;
        }


        response.getWriter().write(EbsConvertUtils.toString(DataError.build(CommonStatus.WRONG_USERNAME_OR_PASSWORD)));

        // Clear the thread-local variable
        CustomAuthenticationFilter.clearLoginRequest();
    }

    private boolean hasException(HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception.getCause() instanceof ProviderNotFoundException) {
            response.getWriter().write(EbsConvertUtils.toString(DataError.build(CommonStatus.ACCOUNT_NOT_FOUND)));
            return true;
        }

        if (exception.getCause() instanceof LockedException) {
            response.getWriter().write(EbsConvertUtils.toString(DataError.build(CommonStatus.TEMPORARY_LOCK_NOT_FINISH)));
            return true;
        }

        if (exception instanceof LockedException) {
            response.getWriter().write(EbsConvertUtils.toString(DataError.build(CommonStatus.ACCOUNT_HAS_BEEN_LOCKED)));
            return true;
        }

        if (exception instanceof DisabledException) {
            response.getWriter().write(EbsConvertUtils.toString(DataError.build(CommonStatus.ACCOUNT_IS_NOT_ACTIVATED)));
            return true;
        }

        return false;
    }
}
