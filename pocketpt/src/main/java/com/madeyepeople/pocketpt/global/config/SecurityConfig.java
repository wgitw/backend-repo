package com.madeyepeople.pocketpt.global.config;

import com.madeyepeople.pocketpt.domain.account.social.CustomAuthorizationFilter;
import com.madeyepeople.pocketpt.domain.account.social.CustomOAuth2UserService;
import com.madeyepeople.pocketpt.domain.account.social.RedirectAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    private final RedirectAuthenticationSuccessHandler redirectAuthenticationSuccessHandler;

    private final CustomAuthorizationFilter customAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
//                .cors((cors) -> cors
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin.disable())
//                .addFilterBefore(new JwtExceptionFilter(), JwtAuthFilter.class)
                .logout((logout) -> logout
                        .logoutUrl("/logout").permitAll()
                        .logoutSuccessUrl("/main")
                )
                .oauth2Login((oauth2Login) -> oauth2Login
//                        .loginPage("/chatlogin")
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService))
                                .successHandler(redirectAuthenticationSuccessHandler)
//                        .failureHandler(authenticationFailureHandler)
//                )
                )
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

//        http.addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}