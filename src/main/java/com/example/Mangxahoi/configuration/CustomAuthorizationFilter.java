package com.example.Mangxahoi.configuration;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.DataError;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.utils.ConvertUtils;
import com.example.Mangxahoi.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final List<String> publicUrls;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    public CustomAuthorizationFilter(String[] publicUrls) {
        this.publicUrls = List.of(publicUrls);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        String servletPath = request.getServletPath();
        boolean isPublicUrl = publicUrls.stream().anyMatch(pattern -> pathMatcher.match(pattern, servletPath));


        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isPublicUrl && authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") ) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(ConvertUtils.toString(DataError.build(CommonStatus.TokenIsInvalid)));
            response.getWriter().flush();
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length());


        try {
            processJwtAuthentication(token);
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException ex) {

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(ConvertUtils.toString(DataError.build(CommonStatus.TokenExpired)));
            response.getWriter().flush();
        } catch (JWTVerificationException ex) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(ConvertUtils.toString(DataError.build(CommonStatus.TokenIsInvalid)));
            response.getWriter().flush();
        }
    }

    private void processJwtAuthentication(String token)  {

        Map<String, Object> claims = TokenUtils.verifyToken(token);
        String email = claims.get("email").toString();
        Long userId =(Long) claims.get("id");
        String username=claims.get("username").toString();
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail(email);


        String[] roles = (String[]) claims.get("roles");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach((role) -> authorities.add(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
