//package com.example.Mangxahoi.configuration;
//
//
//import com.example.Mangxahoi.dto.request.LoginRequest;
//import com.example.Mangxahoi.error.CommonStatus;
//import com.example.Mangxahoi.error.DataError;
//import com.example.Mangxahoi.error.UserStatus;
//import com.example.Mangxahoi.exceptions.EOException;
//import com.example.Mangxahoi.utils.EbsConvertUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//
//import java.io.IOException;
//import java.util.regex.Pattern;
//
//public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private final AuthenticationManager authenticationManager;
//    private static final ThreadLocal<LoginRequest> loginRequestThreadLocal = new ThreadLocal<>();
//    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
//    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            ObjectMapper objectMapper = new ObjectMapper();
//            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
//            loginRequestThreadLocal.set(loginRequest);
//
//            String username = loginRequest.getUsername();
//
//            String password = loginRequest.getPassword();
//            if (!isValidEmail(username)) {
//                response.getWriter().write(EbsConvertUtils.toString(DataError.build(UserStatus.EMAIL_IS_WRONG_FORMAT)));
//                return null ;
//            }
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//            return authenticationManager.authenticate(authenticationToken);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static LoginRequest getLoginRequest() {
//        return loginRequestThreadLocal.get();
//    }
//    private boolean isValidEmail(String email) {
//        return email != null && EMAIL_PATTERN.matcher(email).matches();
//    }
//    public static void clearLoginRequest() {
//        loginRequestThreadLocal.remove();
//    }
//}
