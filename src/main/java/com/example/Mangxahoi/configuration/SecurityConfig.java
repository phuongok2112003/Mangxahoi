package com.example.Mangxahoi.configuration;

import com.example.Mangxahoi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Value("${endpoints.cors.allowed-headers: Authorization, Content-Type}")
    String allowedHeaders;

    @Value("${endpoints.cors.allowed-methods: POST, PUT, GET, OPTIONS, DELETE}")
    List<String> allowedMethods;

    @Value("${endpoints.cors.allowed-origins: *}")
    String allowedOrigins;

    @Value("${endpoints.cors.allowed-credentials: true}")
    boolean allowedCredentials;

    @Value("${endpoints.cors.max-age: 36000}")
    long maxAge;

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenBlacklist tokenBlacklist;
    private final UserService userService;

    private static final String[] PUBLIC_URLS = {
            "/api/**",
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated());

        // Đăng nhập qua CustomAuthenticationFilter
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(null));
        customAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(userService));
        customAuthenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler(userService));
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(tokenBlacklist), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }
}
