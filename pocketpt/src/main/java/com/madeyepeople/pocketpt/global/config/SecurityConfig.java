package com.madeyepeople.pocketpt.global.config;

import com.madeyepeople.pocketpt.domain.account.social.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    private final RedirectAuthenticationSuccessHandler redirectAuthenticationSuccessHandler;

    private final CustomAuthorizationFilter customAuthorizationFilter;

    private final JwtExceptionFilter jwtExceptionFilter;

    @Autowired
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/api/v1/account/main",
                                "/api/v1/account/test-logout",
                                "/api/v1/account/cookie-test",
                                "/api/v1/account/flush-redis",
                                "/ws-stomp"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout").permitAll()
                        .logoutSuccessUrl("/api/v1/test-logout")
                )
                .oauth2Login((oauth2Login) -> oauth2Login
                                .authorizationEndpoint(authorization -> authorization
                                        .baseUri("/oauth2/authorization")
                                        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService))
                                .successHandler(redirectAuthenticationSuccessHandler)
//                        .failureHandler(authenticationFailureHandler)
//                )
                )
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, customAuthorizationFilter.getClass());
        return http.build();
    }
}